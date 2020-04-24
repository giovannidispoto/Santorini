package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.server.actions.data.ActualPlayerResponse;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

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
    private List<DivinityCard> cards;
    private Map<String, ClientHandler> handlers;
    private int lobbySize;

    /**
     * Create controller
     */
    public Controller(){
        this.players = new LinkedList<>();
        this.cards = new ArrayList<>();
        this.handlers = new HashMap<>();
    }

    public void setLobbySize(String playerNickname, int lobbySize){
        this.lobbySize = lobbySize;
    }


    /**
     * Add new player in the game
     * @param playerNickname unique nickname of the player
     * @param color color of the player
     */
    public void addNewPlayer(String playerNickname, Color color) {
        Player p = new Player(playerNickname, color);
        players.add(p);
        Battlefield.getBattlefieldInstance().attach(handlers.get(playerNickname));

        //if(players.size() == lobbySize)
            //startMatch();
    }

    /**
     * Set cqrd to player
     * @param playerNickname player
     * @param card card
     * @throws IOException exception
     */
    public void setPlayerCard(String playerNickname, String card){
        try {
            DeckReader reader = new DeckReader();
            Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
            Player p = getPlayerFromString(playerNickname);
            p.setPlayerCard(d.getDivinityCard(card));
            cards.add(d.getDivinityCard(card));
        }catch(IOException e){
            e.printStackTrace();
        }
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
        match.setCurrentPlayer(players.get(new Random().nextInt(players.size())));
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
    public void playStep(int x, int y){
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


}
