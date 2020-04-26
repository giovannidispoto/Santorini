package it.polimi.ingsw.client.controller;

import java.util.*;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.network.ClientSocketConnection;
import it.polimi.ingsw.client.network.ServerHandler;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicActionInterface;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.*;
import it.polimi.ingsw.client.clientModel.basic.Deck;


/**
 * ClientController Class
 */
public class ClientController {
    //This Player Client-Side
    private String playerNickname;
    private CellInterface[][] workerView;
    //Match
    private List<PlayerInterface> players;
    private List<Integer> workersID;
    private Step turn;
    //Lobby
    private Boolean validNick, lobbyState;
    private String godPlayer;
    private Deck cardsDeck;
    private int currentLobbySize;
    //Utils - Locks for Wait & Notify
    public LockObjects lockObjects;
    public Thread controllerThread;
    //connection & handler
    private ClientSocketConnection socketConnection;
    private ServerHandler serverHandler;

    /**
     * ClientController Constructor
     */
    public ClientController(){
        this.players = new ArrayList<>();
        this.workersID = new ArrayList<>();
        this.lockObjects = new LockObjects();
        this.controllerThread = Thread.currentThread();
    }

    //Start Network

    /**
     *  Initialize a new socket on the controller from which it is called,
     *  the socket is missing the host address of the server to be able to start
     *
     *  setIP: clientController.getSocketConnection().setServerName("String")
     *  startSocket: clientController.getSocketConnection().startConnection()
     */
    public void startNetwork(){
        this.socketConnection = new ClientSocketConnection(this);
    }

    //Launch Interrupt for Controller Thread
    public void interruptController(){
        this.controllerThread.interrupt();
    }

    //Wait Request to Controller

    /** Wait until you receive SetPickedCards message from the server
     *
     * @return  false: if there was an error, true: method performed without errors
     */
    public Boolean waitSetPickedCards(){
        synchronized (lockObjects.lockSetPickedCards){
            return lockObjects.setWait(lockObjects.lockSetPickedCards);
        }
    }

    //Request Messages Area

    /** Communicates to the server the intention to join the game
     *  N.B: Blocking request until a response is received
     *
     * @param playerNickname    NickName Choose by the player
     * @param lobbySize Preferred size of the lobby
     * @return  false: if there was an error, true: method performed without errors
     */
    public Boolean addPlayerRequest(String playerNickname, int lobbySize){
        AddPlayerInterface data = new AddPlayerInterface(playerNickname, lobbySize);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("addPlayer", data)));
        synchronized (lockObjects.lockAddPlayer){
            return lockObjects.setWait(lockObjects.lockAddPlayer);
        }
    }

    /** Communicates to the server the need to get the deck
     *  N.B: Only who is the God Player should make this request
     *  N.B: Blocking request until a response is received
     *
     * @return  false: if there was an error, true: method performed without errors
     */
    public Boolean getDeckRequest(){
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getDeck")));
        synchronized (lockObjects.lockGetDeck){
            return lockObjects.setWait(lockObjects.lockGetDeck);
        }
    }

    /** Communicate to the server the cards chosen by the God Player,
     *  the number of cards chosen must be equal to currentLobbySize
     *
     * @param cards list of strings, where each string is the name of the card chosen in cardsDeck
     */
    public void setPickedCardsRequest(List<String> cards){
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setPickedCards", cards)));
    }

    public void getWorkersIDRequest(String playerNickname){
        SendPlayerNicknameInterface data = new SendPlayerNicknameInterface(playerNickname);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("getWorkersID", data)));
    }

    public Boolean getPlayersRequest(){
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getPlayers")));
        synchronized (lockObjects.lockGetPlayers){
            return lockObjects.setWait(lockObjects.lockGetPlayers);
        }
    }

    public void setInitialWorkerPositionRequest(String playerNickname, int workerID, int row, int col){
        SetInitialWorkerPositionInterface data = new SetInitialWorkerPositionInterface(playerNickname, workerID, row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setInitialWorkerPosition", data)));
    }

    public void selectWorkerRequest(String playerNickname, int workerID) {
        SelectWorkerInterface data = new SelectWorkerInterface(playerNickname, workerID);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("selectWorker", data)));
    }

    public void playStepRequest(int row, int col) {
        PlayStepInterface data = new PlayStepInterface(row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("playStep", data)));
    }

    public void startTurnRequest(String playerNickname, Boolean basicTurn){
        StartTurnInterface data = new StartTurnInterface(playerNickname, basicTurn);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("startTurn", data)));
    }

    public void skipStepRequest() {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("skipStep")));
    }

    //----------------------------------------------------------------------------------------------------------------
    //Getter & Setter
    public CellInterface[][] getWorkerView() {
        return workerView;
    }

    public CellInterface getWorkerViewCell(int x, int y){
        return workerView[x][y];
    }

    public void setWorkerView(CellInterface[][] workerView) {
        this.workerView = workerView;
    }

    public Deck getCardsDeck() {
        return cardsDeck;
    }

    public void setCardsDeck(Deck cardsDeck) {
        this.cardsDeck = cardsDeck;
    }

    public String getGodPlayer() {
        return godPlayer;
    }

    public void setGodPlayer(String godPlayer) {
        this.godPlayer = godPlayer;
    }

    public Boolean getValidNick() {
        return validNick;
    }

    public void setValidNick(Boolean validNick) {
        this.validNick = validNick;
    }

    public Boolean getLobbyState() {
        return lobbyState;
    }

    public void setLobbyState(Boolean lobbyState) {
        this.lobbyState = lobbyState;
    }

    public int getCurrentLobbySize() {
        return currentLobbySize;
    }

    public void setCurrentLobbySize(int currentLobbySize) {
        this.currentLobbySize = currentLobbySize;
    }

    public ClientSocketConnection getSocketConnection() {
        return socketConnection;
    }

    public void setSocketConnection(ClientSocketConnection socketConnection) {
        this.socketConnection = socketConnection;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    /**
     * Associate controller to server handler
     * @param serverHandler server handler
     */
    public void registerHandler(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    public List<PlayerInterface> getPlayers(){
        return players;
    }

    public void setPlayers(List<PlayerInterface> players){
        this.players = players;
    }

    public List<Integer> getWorkersID() {
        return workersID;
    }

    public void setWorkersID(List<Integer> workersID) {
        this.workersID = workersID;
    }
}
