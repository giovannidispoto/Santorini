package it.polimi.ingsw.client.controller;

import java.util.*;

import com.google.gson.Gson;
import it.polimi.ingsw.client.clientModel.basic.Color;
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
    //--    This Player Client-Side
    private String playerNickname;
    private Color playerColor;
    private CellInterface[][] workerView;
    //--    Match
    private List<PlayerInterface> players;
    private List<Integer> workersID;
    private Step turn;
    //--    Lobby
    private boolean validNick;          //Indicates if your nickname is valid
    private boolean lobbyState;         //Indicates if you are registered with a lobby
    private boolean fullLobby;          //Indicates if the server is already busy with a game
    private String godPlayer;           //Player choosing godCards from cardsDeck
    private List<String> godCards;      //Contains Cards chosen by GodPlayer, from which you can choose your card
    private Deck cardsDeck;             //Deck sent by the server, containing the playable cards in this lobby
    private int currentLobbySize;
    //--    Utils - Locks for Wait & Notify
    public LockManager lockManager;
    public Thread controllerThread;
    //--    Connection & handler
    private ClientSocketConnection socketConnection;
    private ServerHandler serverHandler;

    /**
     * ClientController Constructor
     */
    public ClientController(){
        this.players = new ArrayList<>();
        this.workersID = new ArrayList<>();
        this.lockManager = new LockManager();
        this.controllerThread = Thread.currentThread();
    }

    //------    START NETWORK

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

    /**
     * Associate controller to server handler
     * @param serverHandler server handler
     */
    public void registerHandler(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    //TODO: Think about interrupt
    //Launch Interrupt for Controller Thread
    public void interruptController(){
        this.controllerThread.interrupt();
    }

    //------    WAIT REQUESTS to Controller

    /** Wait until you receive SetPickedCards message from the server
     *  N.B: Blocking method until a response is received
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean waitSetPickedCards(){
        synchronized (lockManager.lockSetPickedCards){
            return lockManager.setWait(lockManager.lockSetPickedCards);
        }
    }

    /** Wait until you receive SetPlayerCard message from the server
     *  N.B: Blocking method until a response is received
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean waitSetPlayerCard(){
        synchronized (lockManager.lockSetPlayerCard){
            return lockManager.setWait(lockManager.lockSetPlayerCard);
        }
    }

    /** Wait until you receive SetWorkersID message from the server
     *  N.B: Blocking method until a response is received
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean waitSetWorkersID(){
        synchronized (lockManager.lockSetWorkersID){
            return lockManager.setWait(lockManager.lockSetWorkersID);
        }
    }

    /** Wait until you receive SetBattlefield message from the server
     *  N.B: Blocking method until a response is received
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean waitSetBattlefield(){
        synchronized (lockManager.lockSetBattlefield){
            return lockManager.setWait(lockManager.lockSetBattlefield);
        }
    }

    /** Wait until you receive SetPlayers message from the server
     *  N.B: Blocking method until a response is received
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean waitSetPlayers(){
        synchronized (lockManager.lockSetPlayers){
            return lockManager.setWait(lockManager.lockSetPlayers);
        }
    }

    //------    REQUEST MESSAGES to Server

        //--  REQUESTS IN LOBBY

    /** Communicates to the server the intention to join the game
     *  N.B: Blocking request until a response is received
     *
     * @param playerNickname    NickName Choose by the player
     * @param lobbySize Preferred size of the lobby
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean addPlayerRequest(String playerNickname, int lobbySize){
        AddPlayerInterface data = new AddPlayerInterface(playerNickname, lobbySize);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("addPlayer", data)));
        synchronized (lockManager.lockAddPlayer){
            return lockManager.setWait(lockManager.lockAddPlayer);
        }
    }

    /** Communicates to the server the need to get the deck
     *  N.B: Only who is the God Player should make this request
     *  N.B: Blocking request until a response is received
     *
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean getDeckRequest(){
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getDeck")));
        synchronized (lockManager.lockGetDeck){
            return lockManager.setWait(lockManager.lockGetDeck);
        }
    }

    /** Communicate to the server the cards chosen by the God Player,
     *  the number of cards chosen must be equal to currentLobbySize
     *
     * @param cards list of strings, where each string is the name of the card chosen in cardsDeck
     */
    public void setPickedCardsRequest(List<String> cards){
        PickedCardsInterface data = new PickedCardsInterface(cards);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setPickedCards", data)));
    }

    /** Communicate to the server the card chosen by the Player
     *  (choice between possible cards sent by the server with the mirror command)
     *
     * @param playerNickname    NickName Choose by the player
     * @param cardName  name of the chosen card
     */
    public void setPlayerCardRequest(String playerNickname, String cardName){
        SetPlayerCardInterface data = new SetPlayerCardInterface(playerNickname, cardName);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setPlayerCard", data)));
    }

    /** Client asks the server for Battlefield Update
     *  N.B: Blocking request until a response is received
     *
     * @return  false: if there was an error, true: method performed without errors
     */
    public boolean getBattlefieldRequest(){
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getBattlefield")));
        synchronized (lockManager.lockGetBattlefield){
            return lockManager.setWait(lockManager.lockGetBattlefield);
        }
    }

    /** After requesting the battlefield,
     *  with this method the client chooses where to put the workers on the board (one at a time)
     *  checking not to put it on another player
     *
     * @param playerNickname    NickName Choose by the player
     * @param workersPosition   List containing the ID and position of each worker
     */
    public void setInitialWorkersPositionRequest(String playerNickname, List<WorkerPositionInterface> workersPosition){
        SetInitialWorkersPositionInterface data = new SetInitialWorkersPositionInterface(playerNickname, workersPosition);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setInitialWorkersPosition", data)));
    }

        //--  REQUESTS IN MATCH

    public void selectWorkerRequest(String playerNickname, int workerID) {
        SelectWorkerInterface data = new SelectWorkerInterface(playerNickname, workerID);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("selectWorker", data)));
    }

    public void playStepRequest(int row, int col) {
        PlayStepInterface data = new PlayStepInterface(row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("playStep", data)));
    }

    public void startTurnRequest(String playerNickname, boolean basicTurn){
        StartTurnInterface data = new StartTurnInterface(playerNickname, basicTurn);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("startTurn", data)));
    }

    public void skipStepRequest() {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("skipStep")));
    }

    //-------------------------------------------------------------------------------------------   GETTERS & SETTERS

    //------    USED BY UI:
    public List<String> getGodCards() {
        return godCards;
    }

    public boolean isFullLobby() {
        return fullLobby;
    }

    public Deck getCardsDeck() {
        return cardsDeck;
    }

    public String getGodPlayer() {
        return godPlayer;
    }

    public boolean getValidNick() {
        return validNick;
    }

    public boolean getLobbyState() {
        return lobbyState;
    }

    public int getCurrentLobbySize() {
        return currentLobbySize;
    }

    public ClientSocketConnection getSocketConnection() {
        return socketConnection;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public List<PlayerInterface> getPlayers(){
        return players;
    }

    public List<Integer> getWorkersID() {
        return workersID;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

        //--    WORKER-VIEW

    public CellInterface[][] getWorkerView() {
        return workerView;
    }

    public CellInterface getWorkerViewCell(int x, int y){
        return workerView[x][y];
    }

    //------    USED BY COMMAND PATTERN:

    public void setGodCards(List<String> godCards) {
        this.godCards = godCards;
    }

    public void setFullLobby(boolean fullLobby) {
        this.fullLobby = fullLobby;
    }

    public void setWorkerView(CellInterface[][] workerView) {
        this.workerView = workerView;
    }

    public void setCardsDeck(Deck cardsDeck) {
        this.cardsDeck = cardsDeck;
    }

    public void setGodPlayer(String godPlayer) {
        this.godPlayer = godPlayer;
    }

    public void setValidNick(boolean validNick) {
        this.validNick = validNick;
    }

    public void setLobbyState(boolean lobbyState) {
        this.lobbyState = lobbyState;
    }

    public void setCurrentLobbySize(int currentLobbySize) {
        this.currentLobbySize = currentLobbySize;
    }

    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void setPlayers(List<PlayerInterface> players){
        this.players = players;
    }

    public void setWorkersID(List<Integer> workersID) {
        this.workersID = workersID;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }
}
