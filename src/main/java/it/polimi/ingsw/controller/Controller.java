package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.server.ClientHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Controller {
    private Match match;
    private PlayerTurn turn;

    /**
     *
     * @param match
     */
    public Controller(Match match){
        this.match = match;
    }

    /**
     *
     * @param playerNickName
     * @param date
     * @param color
     * @param card
     */
    public void addNewPlayer(String playerNickName, LocalDate date, Color color, DivinityCard card){
        Player p = new Player(playerNickName, date, color);
        p.setPlayerCard(card);
        match.addPlayer(p);
    }

    /**
     *
     * @return
     */
    public List<Player> getPlayers(){
        return match.getMatchPlayers();
    }

    /**
     *
     * @param player
     * @param clientHandler
     */
    public void addWorkers(Player player, ClientHandler clientHandler){
        Worker w1 = new Worker(player);
        Worker w2 = new Worker(player);
        List<Worker> workerList = new ArrayList<>();
        workerList.add(w1);
        workerList.add(w2);
        player.setPlayerWorkers(workerList);

        //Register observer
        w1.attach(clientHandler);
        w2.attach(clientHandler);

    }

    /**
     *
     */
    public void startMatch(){
        match = new Match(new ArrayList<>(),new ArrayList<>());
    }

    /**
     *
     * @param basic
     */
    public void startTurn(boolean basic){
        this.turn = new PlayerTurn(match.generateTurn(basic));
    }

    /**
     *
     * @param w
     * @param x
     * @param y
     */
    public void playStep(Worker w,int x, int y){
        match.setSelectedWorker(w);
        switch(turn.getCurrentState()){
            case MOVE:
                    turn.move(x,y);
                break;
            case BUILD:
                    turn.build(x,y);
                break;
            case REMOVE:
                    turn.remove(x,y);
                break;
            case END:
                    turn.passTurn();
                break;
        }
    }


}
