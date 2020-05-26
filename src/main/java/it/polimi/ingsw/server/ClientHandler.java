package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.server.actions.CommandFactory;
import it.polimi.ingsw.server.actions.data.*;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static it.polimi.ingsw.PrinterClass.ansiRED;
import static it.polimi.ingsw.PrinterClass.ansiRESET;

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
     */
    public void setTimer(){
        ClientHandler playerHandler = this;
        clientTimeoutTimer = new Timer();
        response(new Gson().toJson(new BasicMessageResponse("ping", null)));

        clientTimeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                System.out.println(ansiRED+"Timeout_"+lobbyManager.getPlayerNickName(playerHandler)+" -isLobbyStart:"+ lobbyStarted +" -isStoppedByServer:"+ isMustStopExecution()+ansiRESET);
                playerDisconnected();
            }
        }, 6000);
    }

    public void playerDisconnected(){
        synchronized (lobbyManager) {
            if (!isMustStopExecution()) {
                System.out.println(ansiRED+"Client-Disconnected-Suddenly_NickName: "+getLobbyManager().getPlayerNickName(this)+ansiRESET);
                if (lobbyStarted) {
                    //end game for all in the lobby
                    if (lobbyManager.getControllerByLobbyID(lobbyID).clientDisconnected()) {
                        lobbyManager.deleteLobby(lobbyID);
                        System.out.println(ansiRED + "Lobby-Deleted_ID: " + lobbyID + ansiRESET);
                    }
                } else {
                    //remove from lobby
                    System.out.println(ansiRED + "Player-Waiting-LobbyStart-Removed: " + lobbyManager.getPlayerNickName(this) + ansiRESET);
                    resetTimeout();
                    lobbyManager.removePlayer(this);
                }
            }
        }
    }

    /**
     * Reset timeout set by ping request
     */
    public void resetTimeout(){
        clientTimeoutTimer.cancel();
    }

    /**
     * Process command received from socket
     * @param m message
     */
    public void process(String m){
        try {
            synchronized (lobbyManager) {
                if ((m.contains("addPlayer") || m.contains("pong")) && !this.lobbyStarted && !this.mustStopExecution) {
                    CommandFactory.from(m).execute(null, this);
                } else if (this.lobbyStarted && !this.mustStopExecution) {
                    this.lobbyManager.getLobbyThreadByLobbyID(this.lobbyID).getLobbyExecutorService().execute(
                            () -> CommandFactory.from(m).execute(this.lobbyManager.getControllerByLobbyID(this.lobbyID), this));
                }
            }

        }catch(JsonParseException e){
            System.out.println(e.getMessage());
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
        System.out.println("Battlefield Updated!");

    }

    /**
     * Send worker view when observer notify change
     * @param workerView workerView
     */
    @Override
    public void update(boolean[][] workerView) {
        response(new Gson().toJson(new BasicMessageResponse("workerViewUpdate", new WorkerViewResponse(workerView))));
        System.out.println("WorkerView Updated!");
    }

    /**
     * Add message to queue
     * @param message to Client
     */
    public void responseQueue(String message) {
        this.messageQueue.add(message);
    }

    public void clientShutDown(){
        synchronized (lobbyManager) {
            response(new Gson().toJson(new BasicMessageResponse("serverError", new BasicErrorMessage("One Client Disconnected - Game Interrupted"))));
            resetTimeout();
            setMustStopExecution();
            clientThread.setSocketShutdown();
            System.out.println(ansiRED+"Client-Deleted_NickName: " + getLobbyManager().getPlayerNickName(this) +ansiRESET);
        }
    }

    //GETTER & SETTER

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public void setLobbyID(UUID lobbyID) {
        this.lobbyID = lobbyID;
    }

    public void setLobbyStart() {
        this.lobbyStarted = true;
    }

    public boolean isMustStopExecution() {
        return mustStopExecution;
    }

    public void setMustStopExecution() {
        this.mustStopExecution = true;
    }

}
