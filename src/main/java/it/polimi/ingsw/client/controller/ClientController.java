package it.polimi.ingsw.client.controller;

import java.util.*;

import com.google.gson.Gson;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.network.ClientSocketConnection;
import it.polimi.ingsw.client.network.ServerHandler;
import it.polimi.ingsw.client.network.messagesInterfaces.basicInterfaces.BasicActionInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.*;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase.*;

/**
 * ClientController Class
 */
public class ClientController {
    //--    This Player Client-Side
    private String playerNickname;
    private Color playerColor;
    private boolean[][] workerView;
    private List<Integer> workersID;
    private Step currentStep;
    //--    Match
    private String actualPlayer;
    private List<PlayerInterface> players;
    //--    Lobby
    private boolean validNick;          //Indicates if your nickname is valid
    private boolean lobbyState;         //Indicates if you are registered with a lobby
    private boolean fullLobby;          //Indicates if the server is already busy with a game
    private String godPlayer;           //Player choosing godCards from cardsDeck
    private List<String> godCards;      //Contains Cards chosen by GodPlayer, from which you can choose your card
    private Deck cardsDeck;             //Deck sent by the server, containing the playable cards in this lobby
    private int currentLobbySize;
    //--    Utils - Locks for Wait & Notify
    private final WaitManager waitManager;
    private final Thread mainThread;
    //--    Connection & handler
    private ClientSocketConnection socketConnection;
    private ServerHandler serverHandler;
    //--    View - Management
    private View userView;
    //--    ERROR - Game Management
    private GameState gameState;
    private SantoriniException gameException;
    //TODO:debug
    public boolean debug = false;

    /**
     * ClientController Constructor
     */
    public ClientController(){
        this.waitManager = new WaitManager();
        this.mainThread = Thread.currentThread();
        this.gameException = new SantoriniException(ExceptionMessages.genericError);
        this.gameState = GameState.START;
    }

    /**
     * Show the battlefield to the user (regardless of the graphic interface chosen)
     */
    public void showToUserBattlefield(){
        userView.printBattlefield();
    }

    //------    START NETWORK - UTILS

    /**
     *  Initialize a new socket on the controller from which it is called,
     *  the socket is missing the host address of the server to be able to start, have a default port that can be changed.
     *
     *  1) setIP:       clientController.getSocketConnection().setServerName("String")
     *  (opt)setPort:   clientController.getSocketConnection().setServerPort("int")
     *  2) startSocket: clientController.getSocketConnection().startConnection()
     */
    public void initializeNetwork(){
        this.socketConnection = new ClientSocketConnection(this);
    }

    /**
     * Associate controller to server handler
     * @param serverHandler server handler
     */
    public void registerHandler(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    //------    Error management / Normal execution interruption

    /** Set an error code ("string") in the exception
     *  It is good to do this before calling launchError,
     *  so as not to have a default error
     *  N.B:    It is recommended to use ExceptionMessages.java to choose error messages
     *
     * @param errorMessage  String representing the error
     */
    public void setGameExceptionMessage(String errorMessage) {
        this.gameException = new SantoriniException(errorMessage);
    }

    /**
     * Interrupts the thread that started the controller first and
     * therefore started executing the program (usually main)
     */
    public void interruptNormalExecution(){
        mainThread.interrupt();
    }

    //------    WAIT REQUESTS to Controller

    /** Wait until you receive SetPickedCards message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void waitSetPickedCards() throws SantoriniException {
        synchronized (WaitManager.waitSetPickedCards){
            waitManager.setWait(WaitManager.waitSetPickedCards, this);
        }
    }

    /** Wait until you receive SetPlayerCard message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void waitSetPlayerCard() throws SantoriniException {
        synchronized (WaitManager.waitSetPlayerCard){
            waitManager.setWait(WaitManager.waitSetPlayerCard, this);
        }
    }

    /** Wait until you receive SetWorkersPosition message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void waitSetWorkersPosition() throws SantoriniException {
        synchronized (WaitManager.waitSetWorkersPosition){
            waitManager.setWait(WaitManager.waitSetWorkersPosition, this);
        }
    }

    /** Wait until you receive ActualPlayer message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void waitActualPlayer() throws SantoriniException {
        synchronized (WaitManager.waitActualPlayer){
            waitManager.setWait(WaitManager.waitActualPlayer, this);
        }
    }

    /** Wait until you receive WorkerViewUpdate message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void waitWorkerViewUpdate() throws SantoriniException {
        synchronized (WaitManager.waitWorkerViewUpdate){
            waitManager.setWait(WaitManager.waitWorkerViewUpdate, this);
        }
    }

    //------    REQUEST MESSAGES to Server

        //--  REQUESTS IN LOBBY

    /** Communicates to the server the intention to join the game
     *  N.B: Blocking request until a response is received
     *
     * @param playerNickname    NickName Choose by the player
     * @param lobbySize Preferred size of the lobby
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void addPlayerRequest(String playerNickname, int lobbySize) throws SantoriniException {
        AddPlayerInterface data = new AddPlayerInterface(playerNickname, lobbySize);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("addPlayer", data)));
        //Wait Server Response
        synchronized (WaitManager.waitAddPlayer){
            waitManager.setWait(WaitManager.waitAddPlayer, this);
        }
    }

    /** Communicates to the server the need to get the deck
     *  N.B: Blocking request until a response is received
     *
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void getDeckRequest() throws SantoriniException {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getDeck")));
        //Wait Server Response
        synchronized (WaitManager.waitGetDeck){
            waitManager.setWait(WaitManager.waitGetDeck, this);
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

    /** Client asks the server for PlayersList Update
     *  N.B: Blocking request until a response is received
     *
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void getPlayersRequest() throws SantoriniException {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getPlayers")));
        //Wait Server Response
        synchronized (WaitManager.waitGetPlayers){
            waitManager.setWait(WaitManager.waitGetPlayers, this);
        }
    }

    /** Client asks the server for Battlefield Update
     *  N.B: Blocking request until a response is received
     *
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void getBattlefieldRequest() throws SantoriniException {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("getBattlefield")));
        //Wait Server Response
        synchronized (WaitManager.waitGetBattlefield){
            waitManager.setWait(WaitManager.waitGetBattlefield, this);
        }
    }

    /** After requesting the battlefield,
     *  with this method the client chooses where to put the workers on the board (one at a time)
     *  checking not to put it on another player
     *
     * @param playerNickname    NickName Choose by the player
     * @param workersPosition   List containing the ID and position of each worker
     */
    public void setWorkersPositionRequest(String playerNickname, List<WorkerPositionInterface> workersPosition){
        SetWorkersPositionInterface data = new SetWorkersPositionInterface(playerNickname, workersPosition);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setWorkersPosition", data)));
    }

        //--  REQUESTS IN MATCH

    /** Client asks the server to start a turn, based on basicTurn decision
     *  N.B: Blocking request until a response is received
     *
     * @param playerNickname    NickName Choose by the player
     * @param basicTurn     true: turn without effects, false: turn with effects from your card
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void setStartTurn(String playerNickname, boolean basicTurn) throws SantoriniException {
        SetStartTurnInterface data = new SetStartTurnInterface(playerNickname, basicTurn);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setStartTurn", data)));
        //Wait Server Response
        synchronized (WaitManager.waitStartTurn){
            waitManager.setWait(WaitManager.waitStartTurn, this);
        }
    }

    /** Client notifies the server, with coordinates of the worker selected by the player,
     *  expecting his workerView as server response
     *  N.B: Blocking request until a response is received
     *
     * @param playerNickname    NickName Choose by the player
     * @param row     selected worker battlefield row coordinate
     * @param col     selected worker battlefield column coordinate
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void selectWorkerRequest(String playerNickname, int row, int col) throws SantoriniException {
        SelectWorkerInterface data = new SelectWorkerInterface(playerNickname, row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("selectWorker", data)));
        //Wait Server Response
        waitWorkerViewUpdate();
    }

    /** Client notifies the server of the action requested by the player for the current step
     *  N.B: Blocking request until a response is received
     *
     * @param row     action battlefield row coordinate
     * @param col     action battlefield column coordinate
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void playStepRequest(int row, int col) throws SantoriniException {
        PlayStepInterface data = new PlayStepInterface(row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("playStep", data)));
        //Wait Server Response
        synchronized (WaitManager.waitPlayStepResponse){
            waitManager.setWait(WaitManager.waitPlayStepResponse, this);
        }
    }

    /** Client notifies the server of the choice of player to skip the current step
     *  N.B: Blocking request until a response is received
     *
     * @throws SantoriniException: if there was an error (usually when normal execution is stopped)
     */
    public void skipStepRequest() throws SantoriniException {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("skipStep")));
        //Wait Server Response
        synchronized (WaitManager.waitSkipStepResponse){
            waitManager.setWait(WaitManager.waitSkipStepResponse, this);
        }
    }

    //-------------------------------------------------------------------------------------------   GETTERS & SETTERS

    //------    USED BY WAIT-MANAGER:
    public SantoriniException getGameException() {
        return gameException;
    }

    //------    USED BY MAIN:
    public void setUserView(View userView) {
        this.userView = userView;
    }

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

    public String getActualPlayer() {
        return actualPlayer;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public GameState getGameState() {
        return gameState;
    }

        //--    WORKER-VIEW

    public boolean[][] getWorkerView() {
        return workerView;
    }

    /** Get the cell boolean in position x,y of the workerView
     *
     * @param x workerView row
     * @param y workerView column
     * @return  Boolean associated with the cell
     */
    public boolean getWorkerViewCell(int x, int y){
        return workerView[x][y];
    }

    /** Check if the workerView is all false and therefore no action is possible
     *
     * @return  true if workerView is all false, false at least one action is possible
     */
    public boolean isInvalidWorkerView(){
        for(int x=0; x < BattlefieldClient.N_ROWS; x++){
            for(int y=0; y < BattlefieldClient.N_COLUMNS; y++){
                if(workerView[x][y]){
                    return false;
                }
            }
        }
        return true;
    }

    //------    USED BY COMMAND PATTERN / LOGIC:

    public void setGodCards(List<String> godCards) {
        this.godCards = godCards;
    }

    public void setFullLobby(boolean fullLobby) {
        this.fullLobby = fullLobby;
    }

    public void setWorkerView(boolean[][] workerView) {
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

    public void setActualPlayer(String actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
