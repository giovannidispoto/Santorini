package it.polimi.ingsw.client.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.clientModel.basic.SelectedWorker;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.fileUtilities.FileManager;
import it.polimi.ingsw.client.network.ClientSocketConnection;
import it.polimi.ingsw.client.network.ServerHandler;
import it.polimi.ingsw.client.network.messagesInterfaces.basicInterfaces.BasicActionInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.*;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase.PlayStepInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase.SelectWorkerInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase.SetStartTurnInterface;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * ClientController Class
 */
public class ClientController {
    //--    This Player Client-Side
    private String playerNickname;
    private Color playerColor;
    private String playerCardName;
    private boolean[][] workerView;
    private List<Integer> workersID;
    private SelectedWorker currentWorker;
    private Step currentStep;
    //--    Match
    private String actualPlayer;
    private List<PlayerInterface> players;
    //--    Lobby
    /**
     * Indicates whether the nickname has been accepted by the server, or it is necessary to choose it again
     */
    private boolean validNick;
    /**
     * Indicates whether the chosen lobby is valid on the server to which the request was sent
     */
    private boolean lobbyState;
    /**
     * Indicates if the server is busy and cannot manage multiple games, it may be because it has reached the maximum lobbies number
     */
    private boolean fullLobby;
    /**
     * Player choosing godCards (for all players) from cardsDeck
     */
    private String godPlayer;
    /**
     * Contains Cards chosen by GodPlayer, from which each player can choose his card
     */
    private List<String> godCards;
    /**
     * Deck sent by the server, containing the playable cards in this lobby
     */
    private Deck cardsDeck;
    /**
     * Lobby size the player attempted to register or register with (depends on lobbyState)
     */
    private int currentLobbySize;
    //--    Utils - Locks for Wait & Notify
    private final WaitManager waitManager;
    private Thread controllerThread;
    //--    Connection & handler
    private ClientSocketConnection socketConnection;
    private ServerHandler serverHandler;
    //--    View - Management
    private View userView;
    //--    ERROR - Game Management
    private GameState gameState;
    private SantoriniException gameException;
    /**
     * Public client logger, where you can print any message
     */
    public final Logger loggerIO;
    //--    FILE - File Management
    /**
     * Contains data read from files
     */
    private final FileManager clientFileManager;

    /**
     * ClientController Constructor:
     * Initializes a new logger and stores the thread that the constructor invoked (so that it can be stopped)
     * see registerControllerThread, if using multiple threads
     *
     */
    public ClientController(){
        //Read From File
        this.clientFileManager = new FileManager();
        this.clientFileManager.readClientSettings();
        //Launch other Utils
        this.waitManager = new WaitManager();
        this.controllerThread = Thread.currentThread();
        this.gameException = new SantoriniException(ExceptionMessages.genericError);
        this.gameState = GameState.START;
        //Launch Logger
        this.loggerIO = start_IO_Logger(clientFileManager.isUniqueLogger());
        //Print on the logger the list containing the messages generated by reading from file
        this.clientFileManager.getFileReadingResults().forEach( (string) -> loggerIO.warning(string+System.lineSeparator()) );
    }

    //------    VIEW - UTILS

    /**
     * Show the battlefield to the user (regardless of the graphic interface chosen)
     */
    public void showToUserBattlefield(){
        userView.printBattlefield();
    }

    //------    START NETWORK - UTILS

    /**
     *  Initialize a new socket on the controller from which it is called,
     *  the socket is missing the host address of the server to be able to start, have a default port that can be changed.<br><br>
     *
     *  1) setIP:       clientController.getSocketConnection().setServerName("String")<br>
     *  (opt)setPort:   clientController.getSocketConnection().setServerPort("int")<br>
     *  2) startSocket: clientController.getSocketConnection().startConnection()<br>
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

    //------    ERROR|EXCEPTION MANAGEMENT / Normal Execution Interruption

    /**
     * If you use a different thread to invoke the functions of the controller,
     * through this function you can register correctly so that you are interrupted in case of error<br><br>
     * - N.B: It is recommended to use only one thread to manage all commands to the controller
     *
     * @param controllerThread Thread that can be stopped in case of errors
     */
    public void registerControllerThread(Thread controllerThread){
        this.controllerThread = controllerThread;
    }

    /** Set an error code ("string") in the exception,<br>
     *  It is the easiest way to throw an exception to the client via the controller<br>
     *  You can pass a null in place of the error string, to keep the default error or the previous exception<br><br>
     *
     *  N.B:    It is recommended to use ExceptionMessages to choose error messages
     *
     * @param errorMessage  String representing the error (Recommended to take it from ExceptionMessages-class)
     * @param gameState     State in which the game will go
     * @param interruptExecution   If true the game will be blocked from its normal execution and will have to handle the exception
     */
    public void setGameExceptionMessage(String errorMessage, GameState gameState, boolean interruptExecution) {
        //Check if the game is already in an error or final state, it is useless to throw another exception
        if(getGameState() != GameState.ERROR && getGameState() != GameState.FINISH) {
            if(null != errorMessage)
                this.gameException = new SantoriniException(errorMessage);

            this.setGameState(gameState);

            if(interruptExecution)
                this.interruptNormalThreadExecution();
        }
    }

    /**
     * Interrupts the controllerThread that started the controller first (or was registered later)-(controllerThread)
     */
    public void interruptNormalThreadExecution(){
        if(null != controllerThread && !controllerThread.isInterrupted())
            controllerThread.interrupt();
    }

    //------    WAIT REQUESTS to Controller

    /** Wait until you receive SetPickedCards message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException game general purpose exception
     */
    public void waitSetPickedCards() throws SantoriniException {
        synchronized (WaitManager.waitSetPickedCards){
            waitManager.setWait(WaitManager.waitSetPickedCards, this);
        }
    }

    /** Wait until you receive SetPlayerCard message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException game general purpose exception
     */
    public void waitSetPlayerCard() throws SantoriniException {
        synchronized (WaitManager.waitSetPlayerCard){
            waitManager.setWait(WaitManager.waitSetPlayerCard, this);
        }
    }

    /** Wait until you receive SetWorkersPosition message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException game general purpose exception
     */
    public void waitSetWorkersPosition() throws SantoriniException {
        synchronized (WaitManager.waitSetWorkersPosition){
            waitManager.setWait(WaitManager.waitSetWorkersPosition, this);
        }
    }

    /** Wait until you receive ActualPlayer message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException game general purpose exception
     */
    public void waitActualPlayer() throws SantoriniException {
        synchronized (WaitManager.waitActualPlayer){
            waitManager.setWait(WaitManager.waitActualPlayer, this);
        }
    }

    /** Wait until you receive WorkerViewUpdate message from the server
     *  N.B: Blocking method until a response is received
     * @throws SantoriniException game general purpose exception
     */
    public void waitWorkerViewUpdate() throws SantoriniException {
        synchronized (WaitManager.waitWorkerViewUpdate){
            waitManager.setWait(WaitManager.waitWorkerViewUpdate, this);
        }
    }

    //------    REQUEST MESSAGES to Server

        //--  REQUESTS IN LOBBY

    /** Communicates to the server the intention to join the game, <br>
     *  Nickname on the server according to the convention, (lowercase, no special char)
     *  N.B: Blocking request until a response is received
     *
     * @param playerNickname    NickName Choose by the player
     * @param lobbySize Preferred size of the lobby
     * @throws SantoriniException game general purpose exception
     */
    public void addPlayerRequest(String playerNickname, int lobbySize) throws SantoriniException {
        //Set In the Controller
        this.setPlayerNickname(playerNickname);
        //Send the Request
        AddPlayerInterface data = new AddPlayerInterface(this.playerNickname, lobbySize);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("addPlayer", data)));
        //Wait Server Response
        synchronized (WaitManager.waitAddPlayer){
            waitManager.setWait(WaitManager.waitAddPlayer, this);
        }
    }

    /** Communicates to the server the need to get the deck
     *  N.B: Blocking request until a response is received
     *
     * @throws SantoriniException game general purpose exception
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

    /** Communicate to the server the card chosen by the Player and save it in the ClientController
     *  (choice between possible cards sent by the server with the mirror command)
     *
     * @param cardName  name of the chosen card
     */
    public void setPlayerCardRequest(String cardName){
        this.playerCardName = cardName;
        //send to server
        SetPlayerCardInterface data = new SetPlayerCardInterface(this.playerNickname, cardName);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setPlayerCard", data)));
    }

    /** Client asks the server for PlayersList Update
     *  N.B: Blocking request until a response is received
     *
     * @throws SantoriniException game general purpose exception
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
     * @throws SantoriniException game general purpose exception
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
     * @param workersPosition   List containing the ID and position of each worker
     */
    public void setWorkersPositionRequest(List<WorkerPositionInterface> workersPosition){
        SetWorkersPositionInterface data = new SetWorkersPositionInterface(this.playerNickname, workersPosition);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("setWorkersPosition", data)));
    }

        //--  REQUESTS IN MATCH

    /** Client asks the server to start a turn, based on basicTurn decision
     *  N.B: Blocking request until a response is received
     *
     * @param basicTurn     true: turn without effects, false: turn with effects from your card
     * @throws SantoriniException game general purpose exception
     */
    public void setStartTurn(boolean basicTurn) throws SantoriniException {
        SetStartTurnInterface data = new SetStartTurnInterface(this.playerNickname, basicTurn);
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
     * @param row     selected worker battlefield row coordinate
     * @param col     selected worker battlefield column coordinate
     * @throws SantoriniException game general purpose exception
     */
    public void selectWorkerRequest(int row, int col) throws SantoriniException {
        SelectWorkerInterface data = new SelectWorkerInterface(this.playerNickname, row, col);
        serverHandler.request(new Gson().toJson(new BasicMessageInterface("selectWorker", data)));
        //Wait Server Response
        waitWorkerViewUpdate();
        this.currentWorker = new SelectedWorker(row,col);
    }

    /** Client notifies the server of the action requested by the player for the current step
     *  N.B: Blocking request until a response is received
     *
     * @param row     action battlefield row coordinate
     * @param col     action battlefield column coordinate
     * @throws SantoriniException game general purpose exception
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
     * @throws SantoriniException game general purpose exception
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

    public String getPlayerCardName() {
        return playerCardName;
    }

    /**
     * Getter to get the current worker who will have to make the action,
     * N.B: It can be null, if it is not your turn
     * @return  selectedWorker object
     */
    public SelectedWorker getCurrentWorker() {
        return currentWorker;
    }

    public FileManager getClientFileManager() {
        return clientFileManager;
    }

        //--    WORKER-VIEW

    public synchronized boolean[][] getWorkerView() {
        return workerView;
    }

    /** Get the cell boolean in position x,y of the workerView
     *
     * @param x workerView row
     * @param y workerView column
     * @return  Boolean associated with the cell
     */
    public synchronized boolean getWorkerViewCell(int x, int y){
        return workerView[x][y];
    }

    /** Check if the workerView is all false and therefore no action is possible
     *
     * @return  true if workerView is all false, false at least one action is possible
     */
    public synchronized boolean isInvalidWorkerView(){
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

    public synchronized void setWorkerView(boolean[][] workerView) {
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

    /**
     * Set the nickname on the server according to the convention, lowercase
     * @param playerNickname String NickName
     */
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname.toLowerCase(Locale.ROOT);
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

    public void setCurrentWorker(SelectedWorker currentWorker) {
        this.currentWorker = currentWorker;
    }

    //------    DEBUG / LOGGER

    /**
     *  Initializes a new logger, which writes a file to the root where the jar / executable is run,
     *  The log file is unique and in TXT format,
     *
     *  If it is impossible to create a new log file, execution continues with an error
     *
     * @param uniqueness boolean that allows you to generate always different files (true)
     * @return  Logger interface on which messages can be sent (info, severe etc.)
     */
    public Logger start_IO_Logger(boolean uniqueness){
        int randomHash = Math.abs(UUID.randomUUID().hashCode());
        String fileID = "groupAM28";
        Logger logger = Logger.getLogger("SantoriniClientLogger");
        FileHandler fileHandler;

        if(uniqueness)
            fileID = Integer.toString(randomHash);

        try {
            // This block configure the logger with handler and formatter
            File f = new File(System.getProperty("java.class.path"));
            File dir = f.getAbsoluteFile().getParentFile();
            String path = dir.toString();
            //System.out.println(path); //debug
            fileHandler = new FileHandler(path + "/" + fileID + "_SantoriniMatch.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            // Set the preferred format
            fileHandler.setFormatter(formatter);
            // Remove console output (remove the console handler)
            logger.setUseParentHandlers(false);

            // Start first message
            logger.info("Started Santorini Client Logger\n");

        } catch (Exception e) {
            System.out.println("FAILED-LOADING-LOGGER\n");
        }
        return logger;
    }

    //------     PING MANAGEMENT

    /**
     * Responds to the server ping message
     */
    public void sendPingResponse() {
        serverHandler.request(new Gson().toJson(new BasicActionInterface("pong")));
    }

    /**
     * Clear the ping timer and reset it
     */
    public void resetPingTimer() {
        serverHandler.resetServerTimeout();
    }
}
