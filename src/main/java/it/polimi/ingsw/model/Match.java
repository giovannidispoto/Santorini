package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;


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
    private List<GlobalCondition> matchGlobalConditions;
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
    public void addPlayer(Player newPlayer){
        if(matchPlayers.contains(newPlayer))
                throw new RuntimeException("Player already in game");
        matchPlayers.add(newPlayer);
    }

    /**
     *
     */
    public void playTurn() {
        Turn t = generateTurn();
        //move
        //build
        //pass turn
        //create worker view based on conditions passed with a function
    }

    /**
     *
     *
     * @return
     */
    private Turn generateTurn(){

        return null;
    }

    public void nextPlayer(){
           currentPlayer = matchPlayers.get((matchPlayers.indexOf(currentPlayer) + 1) % 3);
    }
}
