package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.server.ClientHandler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller Class
 */
public class Controller {
    private Match match;
    private PlayerTurn turn;


    /**
     * Add new player in the game
     * @param playerNickName unique nickname of the player
     * @param date date of birth
     * @param color color of the player
     * @param card divinity card
     */
    public void addNewPlayer(String playerNickName, LocalDate date, Color color, String card) throws IOException {
        DeckReader reader = new DeckReader();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        Player p = new Player(playerNickName, date, color);
        p.setPlayerCard(d.getDivinityCard(card));
        match.addPlayer(p);
    }

    /**
     * Gets match's player
     * @return
     */
    public List<Player> getPlayers(){
        return match.getMatchPlayers();
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
        match = new Match(new ArrayList<>(),new ArrayList<>());

    }

    /**
     * Set first player
     * @param firstPlayer first player
     */
    public void setFirstPlayer(String firstPlayer){
        match.setCurrentPlayer(getPlayerFromString(firstPlayer));
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
     * @param worker worker
     */
    public void selectWorker(String player, int worker){
        match.setSelectedWorker(getWorkerFromString(player, worker));
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
                    turn.move(match.getSelectedWorker(),x,y);
                break;
            case BUILD:
                    turn.build(match.getSelectedWorker(),x,y);
                break;
            case REMOVE:
                    turn.remove(match.getSelectedWorker(),x,y);
                break;
        }
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
        Optional<Player> p = match.getMatchPlayers().stream().filter(pl -> pl.getPlayerNickname().equals(player)).findFirst();
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
