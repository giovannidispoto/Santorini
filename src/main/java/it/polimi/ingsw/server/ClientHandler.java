package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.actions.CommandFactory;
import it.polimi.ingsw.server.actions.data.*;
import it.polimi.ingsw.server.consoleUtilities.PrinterClass;
import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;
import it.polimi.ingsw.server.observers.ObserverBattlefield;
import it.polimi.ingsw.server.observers.ObserverWorkerView;

import java.util.*;

/**
 * ClientHandler execute commands from socket and send response to client.
 * Every Thread has his own ClientHandler, such as a Virtual Client
 */
public class ClientHandler implements ObserverBattlefield, ObserverWorkerView {
    /**
     * Server system printer
     */
    private final PrinterClass consolePrinter;
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
       this.consolePrinter = PrinterClass.getPrinterInstance();
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
        clockPingTimer = new Timer();
        //check if it is necessary to set the default value
        if (pingDelay <= 0)
            pingDelay = 5000;
        try {
            //ping after waiting for : pingDelay (ms)
            clockPingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startPingSchedule();
                }
            }, pingDelay);

        }catch (IllegalStateException e){
            consolePrinter.printMessage(PrinterClass.timerTimeoutError+" clockPingTimer");
        }
    }

    private void startPingSchedule(){
        clientTimeoutTimer = new Timer();
        response(new Gson().toJson(new BasicMessageResponse(NetworkUtilities.PING_ACTION, null)));
        try {
            clientTimeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    consolePrinter.printPingTimeout(getNickName(), lobbyStarted, isMustStopExecution());
                    playerDisconnected();
                }
            }, 10000);
        }catch (IllegalStateException e){
            consolePrinter.printMessage(PrinterClass.timerTimeoutError+" clientTimeoutTimer");
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
                consolePrinter.printClientDisconnected(getNickName());

                if (isLobbyStarted()) {
                    //end game for all in the lobby
                    lobbyManager.clientDisconnected(lobbyID);

                } else {
                    //remove from lobby
                    consolePrinter.printPlayerWaitingRemoved(getNickName());
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
            response(new Gson().toJson(new BasicMessageResponse(NetworkUtilities.SERVER_ERROR_ACTION, new BasicErrorMessage(NetworkUtilities.DISCONNECTION_ERROR))));
            stopClient();
            consolePrinter.printClientDeleted(getNickName());
    }

    /**
     * Notifies the handler that the player has been eliminated from the match but the match is still in progress
     */
    public void playerIsEliminated(){
        setMustStopExecution();
        consolePrinter.printPlayerEliminated(getNickName(),lobbyID);
    }

    /**
     * Notifies the handler that the game has ended successfully,
     * blocks any incoming and outgoing requests for the handler (including ping) & shutdown it's Net-Thread (the client will have to reconnect)
     */
    public void gameEnded(){
        stopClient();
        consolePrinter.printGameEnd(getNickName());
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
     * @param message containing the command
     */
    public void process(String message){
        try {
            if (!isMustStopExecution()) {
                synchronized (lobbyManager) {
                    if (message.matches(NetworkUtilities.addPlayerRegex) && !isLobbyStarted()) {
                        //In the lobby phase the controller is not yet present therefore the only command that can be executed is the addPlayer
                        CommandFactory.from(message).execute(null, this);

                    } else if (isLobbyStarted()) {
                        //In the match phase the commands are executed by the pool that controls the match
                        this.lobbyManager.getExecutorByLobbyID(this.lobbyID).execute( () -> executeCommandInMatch(message) );
                    }
                }
            }
        }catch (JsonParseException e) {
            consolePrinter.printMessage(PrinterClass.jsonParseError);
        }
    }

    /**
     * Executes all commands in the match phase (through the controller)
     * @param message   containing the command
     */
    private void executeCommandInMatch(String message){
        try{
            CommandFactory.from(message).execute(this.lobbyManager.getControllerByLobbyID(this.lobbyID), this);
        }catch (RuntimeException gameException) {
            //Game  Tampering
            consolePrinter.printDebugMessage(gameException.getMessage());
            consolePrinter.printGameTampering(getNickName());
            playerDisconnected();
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
        this.response(new Gson().toJson(new BasicMessageResponse(NetworkUtilities.BATTLEFIELD_UPDATE_ACTION, new CellMatrixResponse(cellInterfaces))));
        consolePrinter.printDebugMessage("Battlefield Updated!");
    }

    /**
     * Send worker view when observer notify change, only if the client is not blocked by the server
     * @param workerView workerView
     */
    @Override
    public void update(boolean[][] workerView) {
        this.response(new Gson().toJson(new BasicMessageResponse(NetworkUtilities.WORKERVIEW_UPDATE_ACTION, new WorkerViewResponse(workerView))));
        consolePrinter.printDebugMessage("WorkerView Updated!");
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

    /**
     * Get the id of the lobby where the handler was registered
     * @return  LOBBY ID
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Set the id of the lobby where the handler was registered
     * @param lobbyID   LOBBY ID
     */
    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    /**
     * Informs the caller if the handler is forced by the server to block its status and all ongoing executions
     * @return true if the Handler is blocked
     */
    public boolean isMustStopExecution() {
        return mustStopExecution;
    }

    /**
     * Informs the handler if it is forced by the server to block its status and all ongoing executions
     */
    public void setMustStopExecution() {
        this.mustStopExecution = true;
    }

    /**
     * Informs the handler that the lobby has been started
     */
    public void setLobbyStart() {
        this.lobbyStarted = true;
    }

    /**
     * Informs if the handler is in a match state (the lobby has been started)
     * @return true if the lobby has been started
     */
    public boolean isLobbyStarted() {
        return lobbyStarted;
    }

    /**
     * Get the nickName registered on the server, if it is not registered a message is returned
     * @return nickName or Not_Registered message
     */
    public String getNickName(){
        return Objects.requireNonNullElse(getLobbyManager().getPlayerNickName(this), "Not_Registered");
    }

}
