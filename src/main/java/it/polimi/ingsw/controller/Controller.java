package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.ServerMain;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.server.network.ClientHandler;
import it.polimi.ingsw.server.consoleUtilities.PrinterClass;
import it.polimi.ingsw.server.fileUtilities.FileManager;
import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;
import it.polimi.ingsw.server.network.messagesInterfaces.*;
import it.polimi.ingsw.server.observers.ObserverPlayers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller Class
 */
public class Controller implements ObserverPlayers {
    private Match match;
    private PlayerTurn turn;
    private final List<Player> players;
    private List<String> playersInLobby;
    private String firstPlayer;
    private final List<DivinityCard> cards;
    private Map<String, ClientHandler> handlers;
    private Deck deck;
    private final List<DivinityCard> pickedCards;
    private final List<Color> colors;
    private final GameState gameState;
    private int lobbySize;
    private UUID lobbyID;
    private LobbyManager lobbyManager;

    /**
     * Returns the [Row] of the currently selected worker
     * @return  Integer [Row], of the selected worker
     */
    public int getSelectedWorkerRow(){
        return match.getSelectedWorker().getRowWorker();
    }

    /**
     * Returns the [Column] of the currently selected worker
     * @return  Integer [Column], of the selected worker
     */
    public int getSelectedWorkerColumn(){
        return match.getSelectedWorker().getColWorker();
    }

    /**
     * Create controller
     */
    public Controller() {
        this.players = new LinkedList<>();
        this.cards = new ArrayList<>();
        this.handlers = new HashMap<>();
        this.pickedCards = new LinkedList<>();
        this.playersInLobby = new LinkedList<>();
        this.colors = new LinkedList<>();
        this.colors.add(Color.BLUE);
        this.colors.add(Color.BROWN);
        this.colors.add(Color.GREY);
        this.lobbySize = 0;
        this.gameState = new GameState();

        try {
            //Read Cards Deck
            DeckReader reader = new DeckReader();
            InputStream fileStream = ServerMain.class.getClassLoader().getResourceAsStream(FileManager.divinitiesCardsPath);
            deck = reader.loadDeck(new InputStreamReader(Objects.requireNonNull(fileStream)));
        }catch(IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    //-----------------------------------------------------------   START MATCH & CLOSE MATCH

    /**
     * Inform the controller that the lobby is properly formed and the game can be started
     * The status of each handler will be changed, in addition,
     * to each player is sent the message containing the God Player (who will choose the cards for everyone)
     *
     * @param lobbySize number of players
     * @param playerNickname nickname of the last player who completed the lobby
     * @param playersInLobby list containing the nicknames of the players
     * @param handlerMap a map that uniquely links their handler to each nickname
     */
    public void lobbyIsReady(int lobbySize, String playerNickname, List<String> playersInLobby, Map<String, ClientHandler> handlerMap, UUID lobbyID, LobbyManager lobbyManager) {
        //Set Lobby Data in Controller
        this.lobbySize = lobbySize;
        this.handlers = handlerMap;
        this.playersInLobby = playersInLobby;
        this.lobbyID = lobbyID;
        this.lobbyManager = lobbyManager;
        //Notify Lobby Ready to Handlers
        this.handlers.forEach((nickName, handler) -> handler.setLobbyStart());
        //Match Start
        this.firstPlayer = playersInLobby.get(0);
        //notify all player who is the first
        String message = new Gson().toJson(new BasicMessageResponse("setPickedCards", new SetPickedCardRequest(firstPlayer)));
        for (String p : playersInLobby) {
            if (p.equals(playerNickname))
                handlers.get(p).responseQueue(message);
            else
                handlers.get(p).response(message);
        }
        //go to next game state
        gameState.nextState();
    }

    //-----------------------------------------------------------


    /**
     * Set picked cards
     * @param cards cards
     */
    public void setPickedCards(List<String> cards){
        if(gameState.getState() != GameStep.PICKING_CARDS) {
            System.out.println("Not waiting this message");
        }
        else {
            for (int i = 0; i < cards.size(); i++) {
                pickedCards.add(deck.getDivinityCard(cards.get(i)));
            }

            requestSelectCard(getNextPlayer(firstPlayer));
            gameState.nextState();
        }
    }

    /**
     *
     */
    private void requestSelectCard(String playerNickname){
        List<String> pickedCardsName = new ArrayList<>();
        for(DivinityCard d : pickedCards)
            pickedCardsName.add(d.getCardName());

        handlers.get(playerNickname).response(new Gson().toJson(new BasicMessageResponse("setPlayerCard", new SetPlayerCardRequest(pickedCardsName))));
    }

    /**
     *
     * @param player
     * @return
     */
    private String getNextPlayer(String player){
        return playersInLobby.get(((playersInLobby.indexOf(player) + 1) % playersInLobby.size()));
    }

    /**
     * Set Card
     * @param playerNickname player
     * @param card card
     */
    public void setPlayerCard(String playerNickname, String card){
        if(gameState.getState() != GameStep.SETTING_CARDS) {
            System.out.println("Not waiting this message");
        }
        else {

            Color c = colors.get(new Random().nextInt(colors.size()));
            colors.remove(c);
            Player p = new Player(playerNickname, c);
            Battlefield.getBattlefieldInstance().attach(handlers.get(playerNickname));
            players.add(p);

            p.setPlayerCard(deck.getDivinityCard(card));
            pickedCards.remove(deck.getDivinityCard(card));

            if (!playerNickname.equals(firstPlayer))
                requestSelectCard(getNextPlayer(playerNickname));
            else {
                setWorkerPositionActualRequest(playerNickname);
                gameState.nextState();

            }
        }

    }

    private void setWorkerPositionRequest(String playerNickname) {
        addWorkers(playerNickname, handlers.get(playerNickname));
        handlers.get(playerNickname).response(new Gson().toJson(new BasicMessageResponse("setWorkersPosition", new SetWorkerPositionRequest(getWorkersId(playerNickname)))));
    }

    private void setWorkerPositionActualRequest(String playerNickname) {
        addWorkers(playerNickname, handlers.get(playerNickname));
        handlers.get(playerNickname).responseQueue(new Gson().toJson(new BasicMessageResponse("setWorkersPosition", new SetWorkerPositionRequest(getWorkersId(playerNickname)))));
    }

    /**
     * Gets deck
     * @return
     */
    public Deck getDeck(){
        deck = deck.getDeckAllowed(lobbySize);
        return deck;
    }


    /**
     * Gets match's player
     * @return players
     */
    public List<Player> getPlayers(){
        return players;
    }

    /**
     * Add new workers for the selected player
     * @param player player
     * @param clientHandler observer
     */
    public void addWorkers(String player, ClientHandler clientHandler){
        Player p = getPlayerFromString(player);
        Worker w1 = new Worker(p);
        Worker w2 = new Worker(p);
        List<Worker> workerList = new ArrayList<>();
        workerList.add(w1);
        workerList.add(w2);
        p.setPlayerWorkers(workerList);
        Battlefield.getBattlefieldInstance().setWorkersInGame(workerList);

        //Register observer
        //w1.attach(clientHandler);
        // w2.attach(clientHandler);

    }

    /**
     * Set initial worker position for the player
     * @param playerNickname player
     * @param worker worker
     * @param x row position
     * @param y col position
     */
    public void setInitialWorkerPosition(String playerNickname, int worker, int x, int y, int worker2, int x2, int y2){
        if(gameState.getState() != GameStep.ADDING_WORKER){
            System.out.println("Not waiting this message");
        }else {
            if (getWorkerFromString(playerNickname, worker) != null && getWorkerFromString(playerNickname, worker2) != null) {

                getWorkerFromString(playerNickname, worker).setWorkerPosition(x, y);
                getWorkerFromString(playerNickname, worker2).setWorkerPosition(x2, y2);
                if (!getNextPlayer(playerNickname).equals(firstPlayer)) {
                    setWorkerPositionRequest(getNextPlayer(playerNickname));
                } else {
                    startMatch(playerNickname);
                    gameState.nextState();
                }
            }
        }
    }

    /**
     * Start Match
     * @param callingPlayer player who call the method, used for choosing if the message should be inserted in the stack or sent
     */
    public void startMatch(String callingPlayer){
        match = new Match(players);
        match.setCurrentPlayer(getPlayerFromString(firstPlayer));
        //adding observer
        match.attach(this);

        for(Player p : players) {
            if (p.getPlayerNickname().equals(callingPlayer)) {
                handlers.get(callingPlayer).responseQueue(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
            }
            else {
                handlers.get(p.getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
            }
        }
    }

    /**
     * Check if request came from the right handler
     * @param playerNickname nickname requested
     * @param handler client handler
     * @return true if nickname is associate to handler, false otherwise
     */
    public boolean checkHandler(String playerNickname, ClientHandler handler){
        return this.handlers.get(playerNickname) == handler;
    }



    /**
     * Start new turn of the player
     * @param playerNickname player
     * @param basicTurn basic turn if true, with effect if false
     */
    public void startTurn(String playerNickname, boolean basicTurn){
        Player p = getPlayerFromString(playerNickname);
        List<Integer> workersId = getWorkersId(playerNickname);
        //Detach all old Observer
        for(Integer w : workersId){
            getWorkerFromString(playerNickname,w).detachAll();
        }
        this.turn = new PlayerTurn(match.generateTurn(basicTurn), match);
        match.setCurrentPlayer(p);
        if(turn.isLoser()){
            match.removePlayer(p);
            checkDeclareWinner();
            handlers.get(playerNickname).responseQueue(new Gson().toJson(new BasicMessageResponse("youLose", null)));
            handlers.get(playerNickname).playerIsEliminated();
        }
    }

    private void checkDeclareWinner(){
        if(match.getMatchPlayers().size() == 1){
            match.declareWinner(match.getMatchPlayers().get(0));
            handlers.get(match.getMatchPlayers().get(0).getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("youWin", null)));
            PrinterClass.getPrinterInstance().printWinner(match.getWinner().getPlayerNickname());
        }
    }

    /**
     * Select worker of the player
     * @param player player
     * @param x row
     * @param y column
     */
    public void selectWorker(String player, ClientHandler handler,int x, int y){
        if(!Battlefield.getBattlefieldInstance().getCell(x,y).getWorker().getOwnerWorker().getPlayerNickname().equalsIgnoreCase(player))
            throw new RuntimeException("Not Your Worker");

        //Removing all old observer
        for(Worker w : getPlayerFromString(player).getPlayerWorkers())
            w.detachAll();

        match.setSelectedWorker(Battlefield.getBattlefieldInstance().getCell(x,y).getWorker());

        for(Worker w : getPlayerFromString(player).getPlayerWorkers())
            w.attach(handler);

        this.turn.sendFirstStepMatrix();
    }

    /**
     * Play step in the turn (move, build or remove)
     * @param x row
     * @param y col
     */
    public Step playStep(int x, int y){
        boolean winner=false;

        switch(turn.getCurrentStep()){
            case MOVE:
            case MOVE_SPECIAL:
            case MOVE_UNTIL:
                winner = turn.move(match.getSelectedWorker(),x,y);
                break;
            case BUILD:
            case BUILD_SPECIAL:
                winner = turn.build(match.getSelectedWorker(),x,y);
                break;
            case REMOVE:
                winner = turn.remove(match.getSelectedWorker(),x,y);
                break;
        }

        if(!winner && turn.getCurrentStep() == Step.END) {
            turn.passTurn();
            handlers.get(match.getCurrentPlayer().getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
        }
        else if(winner){
            declareWinner(match.getWinner());
            //TODO: END GAME
            //For security, lock the current player
        }

        return turn.getCurrentStep();
    }

    private void declareWinner(Player winner) {
        for(Player p : match.getMatchPlayers()){
            if(p.getPlayerNickname().equals(winner.getPlayerNickname()))
                handlers.get(p.getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("youWin", null)));
            else
                handlers.get(p.getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("youLose", null)));
        }

        PrinterClass.getPrinterInstance().printWinner(match.getWinner().getPlayerNickname());
        this.lobbyManager.gameEnded(this.lobbyID);
    }

    /**
     * Gets step state
     * @return step state
     */
    public Step getStepState(){
        return turn.getCurrentStep();
    }

    /**
     * Skip step if possible
     */
    public Step skipStep(){
        turn.skip();
        //if no move left, end the turn
        if(turn.getCurrentStep() == Step.END) {
            turn.passTurn();
            handlers.get(match.getCurrentPlayer().getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
        }

        return turn.getCurrentStep();
    }

    /**
     * End Turn
     */
    public void passTurn(){
        turn.passTurn();
    }

    /**
     *  Gets workers id of the player
     * @param player player
     * @return workers id of the player
     */
    public List<Integer> getWorkersId(String player){
        return getPlayerFromString(player).getPlayerWorkers().stream().map(Worker::getId).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Gets player from the nickname passed
     * @param player player nickname
     * @return player
     */
    private Player getPlayerFromString(String player){
        Optional<Player> p = players.stream().filter(pl -> pl.getPlayerNickname().equals(player)).findFirst();
        if(p.isEmpty())
            throw new RuntimeException("Player Not Found");

        return p.get();
    }

    /**
     * Gets worker from id passed
     * @param player player
     * @param worker worker
     * @return selected worker
     */
    private Worker getWorkerFromString(String player,int worker){
        Optional<Worker> w = getPlayerFromString(player).getPlayerWorkers().stream().filter(workr-> workr.getId() == worker).findFirst();
        if(w.isEmpty()) {
            System.out.println("Worker Not Found");
            return null;
        }

        return w.get();
    }

    /**
     * Check if nickname is valid
     * @param playerNickname
     * @return
     */
    public boolean isValidNickame(String playerNickname) {
        return !playersInLobby.contains(playerNickname);
    }

    /**
     * Check if lobbySize is valid
     * @param lobbySize
     * @return
     */
    public boolean isValidLobby(int lobbySize) {
        return this.lobbySize == 0 || this.lobbySize == lobbySize;
    }

    /**
     * Check if lobby is full
     * @return
     */
    public boolean isFullLobby(){
        return lobbySize != 0 && lobbySize == playersInLobby.size();
    }

    /**
     * get cards in gamee
     */
    public List<DivinityCard> getCardsInGame() {
        if(gameState.getState() == GameStep.CREATE_LOBBY || gameState.getState() == GameStep.PICKING_CARDS){
            System.out.println("No waiting this message");
            return null;
        }

        List<DivinityCard> cards = new ArrayList<>();
        for(Player p : players)
            cards.add(p.getPlayerCard());
        return cards;
    }

    /**
     * Create interface copy of the battlefield
     * @return
     */
    public CellInterface[][] getBattlefield() {
        CellInterface[][] battlefieldInterface = new CellInterface[Battlefield.N_ROWS][Battlefield.N_COLUMNS];
        String player;
        Color workerColor;

        for(int i = 0; i < Battlefield.N_ROWS; i++){
            for(int j = 0; j < Battlefield.N_COLUMNS; j++){
                player = Battlefield.getBattlefieldInstance().getCell(i,j).getWorker() != null ? Battlefield.getBattlefieldInstance().getCell(i,j).getWorker().getOwnerWorker().getPlayerNickname():null;
                workerColor = player != null ? Battlefield.getBattlefieldInstance().getCell(i,j).getWorker().getOwnerWorker().getPlayerColor(): null;
                battlefieldInterface[i][j] = new CellInterface(player,workerColor, Battlefield.getBattlefieldInstance().getCell(i,j).getTower().getHeight(), Battlefield.getBattlefieldInstance().getCell(i,j).getTower().getLastBlock());
            }
        }
        return battlefieldInterface;
    }

    /*Notify client who lose */
    @Override
    public void update(Player removedPlayer) {
        checkDeclareWinner();
        handlers.get(removedPlayer.getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("youLose", null)));
        handlers.get(removedPlayer.getPlayerNickname()).playerIsEliminated();
    }
}
