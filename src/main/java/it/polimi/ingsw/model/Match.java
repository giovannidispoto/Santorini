package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.cards.GlobalEffect;
import it.polimi.ingsw.model.cards.effetcs.basic.BasicTurn;

import java.util.List;
import java.util.Map;


/**
 * The Match class represent the effective match of players (2 o 3)
 */
public class Match {
    private Battlefield matchBoard;
    /*This list is managed in circular way*/
    private List<Player> matchPlayers;
    private List<DivinityCard> matchCards;
    private List<Turn> matchTurn;
    private Map<String,Turn> turnMap;
    private Worker selectedWorker;
    private List<GlobalWinCondition> matchGlobalConditions;
    private List<GlobalEffect> matchGlobalEffects;
    private Player currentPlayer;

    /**
     *
     * @param matchPlayers
     * @param matchCards
     */
    public Match(List<Player> matchPlayers, List<DivinityCard> matchCards) {
        this.matchBoard = Battlefield.getBattelfieldInstance();
        this.matchPlayers = List.copyOf(matchPlayers);
        this.matchCards = List.copyOf(matchCards);
    }

    /**
     *
     * @param choosenPlayer
     */
    public void removePlayer(Player choosenPlayer) {
        matchPlayers.remove(choosenPlayer);
    }

    /**
     *
     * @param newPlayer
     */
    public void addPlayer(Player newPlayer) {
        if (matchPlayers.contains(newPlayer))
            throw new RuntimeException("Player already in game");
        matchPlayers.add(newPlayer);
    }

   public void setSelectedWorker(Worker selectedWorker){
        if(!selectedWorker.getOwnerWorker().equals(currentPlayer))
            throw new RuntimeException("Trying to use worker of another player");

        this.selectedWorker = selectedWorker;
   }

   public Worker getSelectedWorker(){
        return this.selectedWorker;
   }

    /**
     *
     *
     * @return
     */
    public Turn generateTurn(){
        //In questo caso sto giocando con un turno base
        return new BasicTurn(this);
    }

    public void nextPlayer(){
        if(matchPlayers.size() == 3)
           currentPlayer = matchPlayers.get((matchPlayers.indexOf(currentPlayer) + 1) % 3);
        else
            currentPlayer = matchPlayers.get((matchPlayers.indexOf(currentPlayer) + 1) % 2);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void reachLevel3(){
       //controller method
    }

    public Player declareWinner(){
        return currentPlayer;
    }
}
