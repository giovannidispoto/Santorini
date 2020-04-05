package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.cards.GlobalEffect;
import it.polimi.ingsw.model.cards.effects.basic.BasicTurn;
import it.polimi.ingsw.model.cards.effects.basic.BlockUnder;
import it.polimi.ingsw.model.cards.effects.build.DomeEverywhere;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockAbove;
import it.polimi.ingsw.model.cards.effects.move.ExtraMove;
import it.polimi.ingsw.model.cards.effects.move.NoMoveUp;
import it.polimi.ingsw.model.cards.effects.move.PushCharacter;
import it.polimi.ingsw.model.cards.effects.move.SwitchCharacter;

import java.util.List;
import java.util.Map;

/**
 * Match class represents a match performed by a small group of players (2 or 3)
 */
public class Match {
    private Battlefield matchBoard;
    /* Circle List */
    private List<Player> matchPlayers;
    private List<DivinityCard> matchCards;
    private List<Turn> matchTurn;
    private Map<String,Turn> turnMap;
    private Worker selectedWorker;
    private List<GlobalWinCondition> matchGlobalConditions;
    private List<GlobalEffect> matchGlobalEffects;
    private Player currentPlayer;

    /**
     * Class Constructor
     * @param matchPlayers are the players who take part in the game
     * @param matchCards are the cards picked for this game match
     */
    public Match(List<Player> matchPlayers, List<DivinityCard> matchCards) {
        this.matchBoard = Battlefield.getBattlefieldInstance();
        this.matchPlayers = List.copyOf(matchPlayers);
        this.matchCards = List.copyOf(matchCards);
    }

    /**
     * Generates a new turn
     * @return Turn object
     */
    public Turn generateTurn(){
        //In this case I'm playing with a DomeEveryWhere Turn
        //Turn t = new DomeEverywhere();
        //Turn t = new ExtraMove(2);
        //Turn t = new DomeEverywhere();
        //Turn t = new BasicTurn();
        //Turn t = new PushCharacter();
        Turn t = new NoMoveUp();
       t.setCurrentMatch(this);
       return t;
    }

    /**
     * Adds a new player to the match
     * @param newPlayer who has to be added
     */
    public void addPlayer(Player newPlayer) {
        if (matchPlayers.contains(newPlayer))
            throw new RuntimeException("Player already in game");
        matchPlayers.add(newPlayer);
    }

    /**
     * Removes a player from the match
     * @param chosenPlayer who has to be removed
     */
    public void removePlayer(Player chosenPlayer) {
        matchPlayers.remove(chosenPlayer);
    }

    /**
     * Indicates the next player
     */
    public void nextPlayer(){
        if(matchPlayers.size() == 3)
            currentPlayer = matchPlayers.get((matchPlayers.indexOf(currentPlayer) + 1) % 3);
        else
            currentPlayer = matchPlayers.get((matchPlayers.indexOf(currentPlayer) + 1) % 2);
    }

    /**
     * Declares the match winner
     */
    public Player declareWinner(){
        return currentPlayer;
    }

    public Worker getSelectedWorker(){
        return this.selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker){
        if(!selectedWorker.getOwnerWorker().equals(currentPlayer))
            throw new RuntimeException("Trying to use worker of another player");

        this.selectedWorker = selectedWorker;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
