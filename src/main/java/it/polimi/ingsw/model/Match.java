package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.cards.GlobalEffect;
import it.polimi.ingsw.model.cards.effects.basic.BasicTurn;
import it.polimi.ingsw.model.parser.DivinityEffectReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
    private Worker selectedWorker;
    private List<GlobalWinCondition> matchGlobalConditions;
    private List<GlobalEffect> matchGlobalEffects;
    private Map<Player,Turn> playerTurn;
    private Player currentPlayer;
    public Player winner; //debug for testing
    private final String pathToFile = "src/CardsEffect.json";

    /**
     * Class Constructor
     * @param matchPlayers are the players who take part in the game
     * @param matchCards are the cards picked for this game match
     */
    public Match(List<Player> matchPlayers, List<DivinityCard> matchCards){
        this.matchBoard = Battlefield.getBattlefieldInstance();
        this.matchPlayers = List.copyOf(matchPlayers);
        this.matchCards = List.copyOf(matchCards);
        this.playerTurn = new HashMap<>();
        DivinityEffectReader reader = new DivinityEffectReader();
        try {
            Map<String, Turn> res = reader.load(new FileReader(pathToFile));
            for (Player p : matchPlayers) {
                playerTurn.put(p, res.get(p.getPlayerCard().getCardName()));
                playerTurn.get(p).setCurrentMatch(this);
            }
        }catch(IOException e){
           e.printStackTrace();
        }
    }

    /**
     * Generates a new turn
     * @param basic request basic turn
     * @return Turn object
     */
    public Turn generateTurn(boolean basic){
       if(basic) {
           Turn t = new BasicTurn();
           t.setCurrentMatch(this);
           return t;
       }

       Turn t = playerTurn.get(currentPlayer);
       t.resetTurn();
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
     * @param winner is the winner player
     */
    public void declareWinner(Player winner){
        //How represent winner win? (discussion on slack)
        this.winner = winner;//debug
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
