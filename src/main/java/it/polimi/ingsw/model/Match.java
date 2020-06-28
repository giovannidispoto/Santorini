package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.effects.global.TowersCondition;
import it.polimi.ingsw.server.fileUtilities.FileManager;
import it.polimi.ingsw.ServerMain;
import it.polimi.ingsw.model.cards.effects.basic.BasicTurn;
import it.polimi.ingsw.model.cards.effects.global.GlobalEffect;
import it.polimi.ingsw.model.cards.effects.global.GlobalWinCondition;
import it.polimi.ingsw.model.cards.effects.global.NoLevelUpCondition;
import it.polimi.ingsw.model.parser.DivinityEffectReader;
import it.polimi.ingsw.server.observers.ObserverPlayers;
import it.polimi.ingsw.server.observers.SubjectPlayers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Match class represents a match performed by a small group of players (2 or 3)
 */
public class Match implements SubjectPlayers {
    /* Circle List */
    private final List<Player> matchPlayers;
    private List<Turn> matchTurn;
    private List<ObserverPlayers> observerPlayers;
    private Worker selectedWorker;
    private final List<GlobalWinCondition> matchGlobalConditions;
    private final List<GlobalEffect> matchGlobalEffects;
    private final Map<Player,Turn> playerTurn;
    private Player currentPlayer;
    public Player winner; //debug for testing

    /**
     * Class Constructor
     * @param matchPlayers are the players who take part in the game
     */
    public Match(List<Player> matchPlayers){
        observerPlayers = new ArrayList<>();
        //Set GlobalConditions
        this.matchGlobalConditions = new ArrayList<>();
        matchPlayers.forEach((player) -> {
            if(player.getPlayerCard().getCardName().equalsIgnoreCase("CHRONUS")){
                this.matchGlobalConditions.add(new TowersCondition(player, this,5));
            }
        });
        //Set GlobalEffects
        this.matchGlobalEffects = new ArrayList<>();
        matchPlayers.forEach((player) -> {
            if(player.getPlayerCard().getCardName().equalsIgnoreCase("ATHENA")){
                NoLevelUpCondition.getInstance().restoreEffect();
                NoLevelUpCondition.getInstance().setAssociatedPlayer(player);
                this.matchGlobalEffects.add(NoLevelUpCondition.getInstance());
            }
        });

        //Set Match
        Battlefield.getBattlefieldInstance();
        this.matchPlayers = new ArrayList<> (matchPlayers);
        this.playerTurn = new HashMap<>();
        DivinityEffectReader reader = new DivinityEffectReader();
        try {
            InputStream fileStream = ServerMain.class.getClassLoader().getResourceAsStream(FileManager.cardsEffectPath);
            Map<String, Turn> res = reader.load(new InputStreamReader(Objects.requireNonNull(fileStream)));
            for (Player p : matchPlayers) {
                playerTurn.put(p, res.get(p.getPlayerCard().getCardName()));
                playerTurn.get(p).setCurrentMatch(this);
            }
        }catch(Exception e){
           e.printStackTrace();
        }
    }

    /**
     * Check the Global conditions
     */
    public void checkMatchGlobalConditions() {
        if(matchGlobalConditions.size() != 0)
            matchGlobalConditions.forEach(GlobalWinCondition::checkWinCondition);
    }

    /**
     * Check and apply the global effects on the player passed by parameter,
     * remember the effects are applied on the workerView
     * @param selectedWorker The worker to apply the effects to
     */
    public void checkMatchGlobalEffects(Worker selectedWorker) {
        if(matchGlobalEffects.size() != 0)
            matchGlobalEffects.forEach(globalEffect -> selectedWorker.setWorkerView(globalEffect.applyEffect(selectedWorker)));
    }

    /**
     * Gets winner
     * @return Player object = Winner
     */
    public Player getWinner() {
        return winner;
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
        Battlefield.getBattlefieldInstance().removeWorkers(chosenPlayer);   //clean battlefield
        matchPlayers.remove(chosenPlayer);  //clean match

        notify(chosenPlayer);
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
     * DeclARES the match winner
     * @param winner is the winner player
     */
    public void declareWinner(Player winner){
        //How represent winner win? (discussion on slack)
        this.winner = winner;//debug
    }

    public List<Player> getMatchPlayers(){
        return this.matchPlayers;
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

    public Player getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Add observer
     * @param o observer
     */
    @Override
    public void attach(ObserverPlayers o) {
        observerPlayers.add(o);
    }

    /**
     * Detach observer
     * @param o observer
     */
    @Override
    public void detach(ObserverPlayers o) {
        observerPlayers.remove(o);
    }

    /**
     * detach all observer
     */
    @Override
    public void detachAll() {
        observerPlayers.clear();
    }

    /**
     * Notify observer
     * @param removedPlayer removed Player
     */
    @Override
    public void notify(Player removedPlayer) {
        for(ObserverPlayers o : observerPlayers)
            o.update(removedPlayer);
    }
}
