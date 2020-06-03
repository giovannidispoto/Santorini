package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.actions.CommandFactory;
import it.polimi.ingsw.server.actions.data.*;
import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;
import it.polimi.ingsw.server.observers.ObserverBattlefield;
import it.polimi.ingsw.server.observers.ObserverWorkerView;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

/**
 * ClientHandler execute commands from socket and send response to client.
 * Every Thread has his own ClientHandler, such as a Virtual Client
 */
public class ClientHandler implements ObserverBattlefield, ObserverWorkerView {
    //Lobby Data
    private final LobbyManager lobbyManager;
    private boolean lobbyStarted;
    private UUID lobbyID;
    //ClientHandler Data
    private final ClientThread clientThread;
    private final Stack<String> messageQueue;
    private Timer clientTimeoutTimer;
    private Timer clockPingTimer;
    private boolean mustStopExecution;

    /**
     * Create ClientHandler
     * @param lobbyManager manage lobbies
     * @param clientThread socket thread
     */
    public ClientHandler(LobbyManager lobbyManager, ClientThread clientThread){
       this.lobbyManager = lobbyManager;
       this.lobbyStarted = false;
       this.clientThread = clientThread;
       this.messageQueue = new Stack<>();
       this.clientTimeoutTimer = new Timer();
       this.mustStopExecution = false;
    }

    /**
     * Execute ping to the client (with a certain cadence: pingDelay), expecting a feedback from client before timeout,
     * If the timer expires the client is considered disconnected
     * @param pingDelay milliseconds after which to ping (clockPingTimer), if <= 0 : set to default value = 5000
     */
    public void setTimer(int pingDelay){
        ClientHandler playerHandler = this;
        clientTimeoutTimer = new Timer();
        clockPingTimer = new Timer();
        //check if it is necessary to set the default value
        if (pingDelay <= 0)
            pingDelay = 5000;
        try {
            //ping after waiting for : pingDelay (ms)
            clockPingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    response(new Gson().toJson(new BasicMessageResponse("ping", null)));
                    try {
                        clientTimeoutTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println(ansiRED + "Timeout_" + ansiRESET + lobbyManager.getPlayerNickName(playerHandler) + " -isLobbyStart:" + lobbyStarted + " -isStoppedByServer:" + isMustStopExecution());
                                playerDisconnected();
                            }
                        }, 10000);
                    }catch (IllegalStateException e){
                        System.out.println(timerTimeoutError);
                    }
                }
            }, pingDelay);

        }catch (IllegalStateException e){
            System.out.println(timerTimeoutError);
        }
    }

    /**
     * Reset Timeout set by ping request
     */
    public void resetTimeout(){
        clientTimeoutTimer.cancel();
        clockPingTimer.cancel();
    }

    /**
     * Function invoked the first time a client disconnects
     * Takes care of checking in what state the client was (in play or waiting for the lobby)
     * and if he had already been blocked by disconnecting another player
     */
    public void playerDisconnected(){
        synchronized (lobbyManager) {
            if (!isMustStopExecution()) {
                System.out.println(ansiRED + "Client-Disconnected-Suddenly_NickName: " + getLobbyManager().getPlayerNickName(this) + ansiRESET);

                if (isLobbyStarted()) {
                    //end game for all in the lobby
                    lobbyManager.clientDisconnected(lobbyID);

                } else {
                    //remove from lobby
                    System.out.println(ansiRED + "Player-Waiting-LobbyStart-Removed: " + lobbyManager.getPlayerNickName(this) + ansiRESET);
                    stopClient();
                    lobbyManager.removePlayer(this);
                }
            }
        }
    }

    /**
     * Notifies the handler that a client has disconnected from the game,
     * the handler will take care of notifying the client and closing the connection
     */
    public void disconnectionShutDown(){
            response(new Gson().toJson(new BasicMessageResponse("serverError", new BasicErrorMessage("One Client Disconnected - Game Interrupted"))));
            stopClient();
            System.out.println(ansiRED+"Client-Deleted_NickName: " + getLobbyManager().getPlayerNickName(this) +ansiRESET);
    }

    /**
     * Notifies the handler that the player has been eliminated from the match but the match is still in progress
     */
    public void playerIsEliminated(){
        setMustStopExecution();
        System.out.println(ansiBLUE+"Player: "+lobbyManager.getPlayerNickName(this)+" Eliminated from the game: "+lobbyID+ansiRESET);
    }

    /**
     * Notifies the handler that the game has ended successfully,
     * blocks any incoming and outgoing requests for the handler (including ping) & shutdown it's Net-Thread (the client will have to reconnect)
     */
    public void gameEnded(){
        stopClient();
        System.out.println(ansiRED+"GameEnd_NickName: " + getLobbyManager().getPlayerNickName(this) +ansiRESET);
    }

    /**
     * Stop the ping, the handler and the thread that manages the client
     */
    private void stopClient(){
        setMustStopExecution();
        clientThread.socketShutdown();
        resetTimeout();
    }

    /**
     * Process command received from socket, only if the client is not blocked by the server<br>
     * If the player makes an illegal move, the game is ended, considering him as responsible<br>
     * (Do not send the ping / pong message)
     * @param m message
     */
    public void process(String m){
        try {
            if (!isMustStopExecution()) {
                synchronized (lobbyManager) {
                    if (m.contains("addPlayer") && !isLobbyStarted()) {
                        CommandFactory.from(m).execute(null, this);

                    } else if (isLobbyStarted()) {

                        this.lobbyManager.getExecutorByLobbyID(this.lobbyID).execute(
                                () -> {
                                    try{
                                        CommandFactory.from(m).execute(this.lobbyManager.getControllerByLobbyID(this.lobbyID), this);
                                    }catch (RuntimeException gameException) {
                                        if(printDebugInfo)  System.out.println(gameException.getMessage());
                                        System.out.println(ansiMAGENTA+"!!!-> Player: "+lobbyManager.getPlayerNickName(this)+" Disconnected From The Game by Server, Cause: Game Tampering <-!!!"+ansiRESET);
                                        playerDisconnected();
                                        //TODO: testing, exception during game shutdown all client
                                    }
                                }
                        );
                    }
                }
            }
        }catch (JsonParseException e) {
            System.out.println(jsonParseError);
        }
    }

    /**
     * Send message to client, only if the client is not blocked by the server
     * @param m message
     */
    public void response(String m){
        if(!isMustStopExecution())
            this.clientThread.send(m);
    }

    /**
     * Send all message in queue, only if the client is not blocked by the server
     */
    public void sendMessageQueue(){
       while(!messageQueue.empty())
           this.response(messageQueue.pop());
    }
    


    /**
     * Send Battlefield when observer notify change, only if the client is not blocked by the server
     * @param cellInterfaces matrix
     */
    @Override
    public void update(CellInterface[][] cellInterfaces) {
        this.response(new Gson().toJson(new BasicMessageResponse("battlefieldUpdate", new CellMatrixResponse(cellInterfaces))));

        if(printDebugInfo)  System.out.println("Battlefield Updated!");
    }

    /**
     * Send worker view when observer notify change, only if the client is not blocked by the server
     * @param workerView workerView
     */
    @Override
    public void update(boolean[][] workerView) {
        this.response(new Gson().toJson(new BasicMessageResponse("workerViewUpdate", new WorkerViewResponse(workerView))));

        if(printDebugInfo)  System.out.println("WorkerView Updated!");
    }

    /**
     * Add message to queue
     * @param message to Client
     */
    public void responseQueue(String message) {
        this.messageQueue.add(message);
    }

    //------------------------------------- GETTER & SETTER

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public boolean isMustStopExecution() {
        return mustStopExecution;
    }

    public void setMustStopExecution() {
        this.mustStopExecution = true;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    public void setLobbyStart() {
        this.lobbyStarted = true;
    }

    public boolean isLobbyStarted() {
        return lobbyStarted;
    }

}
