package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.server.actions.data.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller Class
 */
public class Controller {
    private Match match;
    private PlayerTurn turn;
    private List<Player> players;
    private List<String> playersInLobby;
    private String firstPlayer;
    private List<DivinityCard> cards;
    private Map<String, ClientHandler> handlers;
    private Deck deck;
    private List<DivinityCard> pickedCards;
    private List<Color> colors;
    private GameState gameState;
    private int lobbySize;

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
            DeckReader reader = new DeckReader();
            deck = reader.loadDeck(new FileReader("src/Divinities.json"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }


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
     * Add new player in the game
     * @param playerNickname unique nickname of the player
     * @param lobbySize lobby size
     * @return true if player was add, false if there was an error with phase
     */
    public synchronized boolean addNewPlayer(String playerNickname, int lobbySize) {
        if(gameState.getState() != GameStep.CREATE_LOBBY){
                System.out.print("Not waiting this message");
                return false;
        }else {

            if (this.lobbySize == 0) {
                this.lobbySize = lobbySize;
            }

            this.playersInLobby.add(playerNickname);


            if (playersInLobby.size() == lobbySize) {
                //startMatch();
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
            return true;
        }

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
        match = new Match(players,cards);
        match.setCurrentPlayer(getPlayerFromString(firstPlayer));
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
     * Associate nickname to a handler
     * @param playerNickname player nickname
     * @param handler client handler
     */
    public void registerHandler(String playerNickname, ClientHandler handler){
        this.handlers.put(playerNickname, handler);
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
        this.turn = new PlayerTurn(match.generateTurn(basicTurn), match);
        match.setCurrentPlayer(p);
        if(turn.isLoser()){
            match.removePlayer(p);
            checkDeclareWinner();
            handlers.get(playerNickname).responseQueue(new Gson().toJson(new BasicMessageResponse("youLose", null)));
        }
    }

    private void checkDeclareWinner(){
        if(match.getMatchPlayers().size() == 1){
            match.declareWinner(match.getMatchPlayers().get(0));
            handlers.get(match.getMatchPlayers().get(0).getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("youWin", null)));
        }
    }

    /**
     * Select worker of the player
     * @param player player
     * @param x row
     * @param y column
     */
    public boolean[][] selectWorker(String player, ClientHandler handler,int x, int y){
        if(!Battlefield.getBattlefieldInstance().getCell(x,y).getWorker().getOwnerWorker().getPlayerNickname().equalsIgnoreCase(player))
            throw new RuntimeException("Not Your Worker");

        //Removing all old observer
        for(Worker w : getPlayerFromString(player).getPlayerWorkers())
            w.detachAll();

        match.setSelectedWorker(Battlefield.getBattlefieldInstance().getCell(x,y).getWorker());

        //Add observer
        Battlefield.getBattlefieldInstance().getCell(x,y).getWorker().attach(handler);
        this.turn.updateMovmentMatrix();
        boolean[][] workerView = new boolean[Battlefield.N_ROWS][Battlefield.N_COLUMNS];

        for(int i = 0; i < Battlefield.N_ROWS_VIEW; i++){
            for(int j = 0; j < Battlefield.N_COLUMNS_VIEW; j++){
                workerView[i][j] = match.getSelectedWorker().getWorkerView()[i][j] != null;
            }
        }


        return workerView;
    }

    /**
     * Play step in the turn (move, build or remove)
     * @param x row
     * @param y col
     */
    public Step playStep(int x, int y){
        switch(turn.getCurrentState()){
            case MOVE:
            case MOVE_SPECIAL:
            case MOVE_UNTIL:
               boolean winner = turn.move(match.getSelectedWorker(),x,y);
                if(winner){
                    delcareWinner(match.getWinner());
                    System.out.println("Winner: "+ match.getWinner().getPlayerNickname());
                }
                break;
            case BUILD:
            case BUILD_SPECIAL:
                turn.build(match.getSelectedWorker(),x,y);
                break;
            case REMOVE:
                turn.remove(match.getSelectedWorker(),x,y);
                break;
        }
        if(turn.getCurrentState() == Step.END) {
            turn.passTurn();
            handlers.get(match.getCurrentPlayer().getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
        }

        return turn.getCurrentState();
    }

    private void delcareWinner(Player winner) {
        for(Player p : match.getMatchPlayers()){
            if(p.getPlayerNickname().equals(winner.getPlayerNickname()))
                handlers.get(p.getPlayerNickname()).responseQueue(new Gson().toJson(new BasicMessageResponse("youWin", null)));
            else
                handlers.get(p.getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("youLose", null)));
        }

    }

    /**
     * Gets step state
     * @return step state
     */
    public Step getStepState(){
        return turn.getCurrentState();
    }

    /**
     * Skip step if possible
     */
    public Step skipStep(){
        turn.skip();
        //if no move left, end the turn
        if(turn.getCurrentState() == Step.END) {
            turn.passTurn();
            handlers.get(match.getCurrentPlayer().getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
        }

        return turn.getCurrentState();
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
}
