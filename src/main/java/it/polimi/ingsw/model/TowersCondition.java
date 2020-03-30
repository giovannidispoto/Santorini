package it.polimi.ingsw.model;

/**
 * TowersCondition class represents a global effect
 */
public class TowersCondition extends GlobalWinCondition {

    private int numberOfFullTowers;

    /**
     * Class constructor
     */
    public TowersCondition(Player player, Match match,int numberOfFullTowers) {
        super(player,match);
        this.numberOfFullTowers = numberOfFullTowers;
    }

    /**
     * Player wins if there are a certain value of full towers built on the ground
     */
    @Override
    public void checkWinCondition() {
        //win condition
    }
}
