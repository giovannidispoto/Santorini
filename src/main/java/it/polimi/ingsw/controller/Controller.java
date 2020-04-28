package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.server.actions.data.ActualPlayerResponse;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.SetPickedCardRequest;
import it.polimi.ingsw.server.actions.data.SetPlayerCardRequest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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
       for(int i = 0; i < cards.size(); i++){
           pickedCards.add(deck.getDivinityCard(cards.get(i)));
       }

        requestSelectCard(getNextPlayer(firstPlayer));
    }

    /**
     *
     */
    private void requestSelectCard(String playerNickname){
        List<String> pickedCardsName = new ArrayList<>();
        for(DivinityCard d : pickedCards)
            pickedCardsName.add(d.getCardName());

        handlers.get(playerNickname).response(new Gson().toJson(new BasicMessageInterface("setPlayerCard", new SetPlayerCardRequest(pickedCardsName))));
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

        Color c = colors.get(new Random().nextInt(colors.size()));
        colors.remove(c);
        Player p = new Player(playerNickname,c) ;
        Battlefield.getBattlefieldInstance().attach(handlers.get(playerNickname));
        players.add(p);

        p.setPlayerCard(deck.getDivinityCard(card));
        pickedCards.remove(deck.getDivinityCard(card));

        if(!playerNickname.equals(firstPlayer))
            requestSelectCard(getNextPlayer(playerNickname));
    }



    /**
     * Add new player in the game
     * @param playerNickname unique nickname of the player
     * @param lobbySize lobby size
     */
    public void addNewPlayer(String playerNickname, int lobbySize) {

        if(this.lobbySize == 0){
            this.lobbySize = lobbySize;
        }

        this.playersInLobby.add(playerNickname);


        if(playersInLobby.size() == lobbySize){
            //startMatch();
           this.firstPlayer = playersInLobby.get(0);
           //notify all player who is the first
           for(String p : playersInLobby)
                handlers.get(p).response(new Gson().toJson(new BasicMessageInterface("setPickedCardRequest", new SetPickedCardRequest(firstPlayer))));
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
        w1.attach(clientHandler);
        w2.attach(clientHandler);

    }

    /**
     * Set initial worker position for the player
     * @param player player
     * @param worker worker
     * @param x row position
     * @param y col position
     */
    public void setInitialWorkerPosition(String player, int worker, int x, int y){
        getWorkerFromString(player,worker).setWorkerPosition(x,y);
    }

    /**
     * Start Match
     */
    public void startMatch(){
        match = new Match(players,cards);
        match.setCurrentPlayer(getPlayerFromString(firstPlayer));
        handlers.get(match.getCurrentPlayer().getPlayerNickname()).response(new Gson().toJson(new BasicMessageResponse("actualPlayer", new ActualPlayerResponse(match.getCurrentPlayer().getPlayerNickname()))));
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
     * @param basic basic turn if true, with effect if false
     * @param player player
     */
    public void startTurn(boolean basic, String player){
        Player p = getPlayerFromString(player);
        this.turn = new PlayerTurn(match.generateTurn(basic), match);
        match.setCurrentPlayer(p);
    }

    /**
     * Select worker of the player
     * @param player player
     * @param x row
     * @param y column
     */
    public void selectWorker(String player, int x, int y){
        if(!Battlefield.getBattlefieldInstance().getCell(x,y).getWorker().getOwnerWorker().getPlayerNickname().equalsIgnoreCase(player))
            throw new RuntimeException("Not Your Worker");
        match.setSelectedWorker(Battlefield.getBattlefieldInstance().getCell(x,y).getWorker());
        this.turn.updateMovmentMatrix();
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
                turn.move(match.getSelectedWorker(),x,y);
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
    public void skipStep(){
        turn.skip();
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
        if(w.isEmpty())
            throw new RuntimeException("Worker Not Found");

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
     *
     */
    public List<String> getCardsInGame() {
        List<String> cards = new ArrayList<>();
        for(Player p : players)
            cards.add(p.getPlayerCard().getCardName());
        return cards;
    }
}
