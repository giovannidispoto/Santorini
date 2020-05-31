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
     * Execute ping to the client, expecting a feedback before timeout
     * If the timer expires the client is considered disconnected
     */
    public void setTimer(){
        ClientHandler playerHandler = this;
        clientTimeoutTimer = new Timer();
        response(new Gson().toJson(new BasicMessageResponse("ping", null)));

        clientTimeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                    System.out.println(ansiRED + "Timeout_" + lobbyManager.getPlayerNickName(playerHandler) + " -isLobbyStart:" + lobbyStarted + " -isStoppedByServer:" + isMustStopExecution() + ansiRESET);
                    playerDisconnected();
            }
        }, 6000);
    }

    /**
     * Reset timeout set by ping request
     */
    public void resetTimeout(){
        clientTimeoutTimer.cancel();
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
                    lobbyManager.getControllerByLobbyID(lobbyID).clientDisconnected();
                    lobbyManager.deleteLobby(lobbyID);
                    System.out.println(ansiRED + "Lobby-Deleted_ID: " + lobbyID + ansiRESET);

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
        resetTimeout();
        System.out.println(ansiBLUE+"Player: "+lobbyManager.getPlayerNickName(this)+" Eliminated from the game: "+lobbyID+ansiRESET);
    }

    /**
     * Stop the ping, the handler and the thread that manages the client
     */
    private void stopClient(){
        setMustStopExecution();
        resetTimeout();
        clientThread.socketShutdown();
    }

    /**
     * Process command received from socket
     * @param m message
     */
    public void process(String m){
        if(!isMustStopExecution()) {
            synchronized (lobbyManager) {
                try {
                    if ((m.contains("addPlayer") && !isLobbyStarted()) || m.contains("pong")) {
                        CommandFactory.from(m).execute(null, this);

                    } else if (isLobbyStarted()) {

                        this.lobbyManager.getLobbyThreadByLobbyID(this.lobbyID).getLobbyExecutorService().execute(
                                () -> CommandFactory.from(m).execute(this.lobbyManager.getControllerByLobbyID(this.lobbyID), this));
                    }

                } catch (JsonParseException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * Send message to client
     * @param m message
     */
    public void response(String m){
        this.clientThread.send(m);
    }

    /**
     * Send all message in queue
     */
    public void sendMessageQueue(){
       while(!messageQueue.empty())
           this.response(messageQueue.pop());
    }
    


    /**
     * Send Battlefield when observer notify change
     * @param cellInterfaces matrix
     */
    @Override
    public void update(CellInterface[][] cellInterfaces) {
        response(new Gson().toJson(new BasicMessageResponse("battlefieldUpdate", new CellMatrixResponse(cellInterfaces))));

        if(printDebugInfo)  System.out.println("Battlefield Updated!");
    }

    /**
     * Send worker view when observer notify change
     * @param workerView workerView
     */
    @Override
    public void update(boolean[][] workerView) {
        response(new Gson().toJson(new BasicMessageResponse("workerViewUpdate", new WorkerViewResponse(workerView))));

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
