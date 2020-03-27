package it.polimi.ingsw.model;

public class TowersCondition extends GlobalWinCondition {
    private int numberOfFullTowers;

    public TowersCondition(Player player, Match match,int numberOfFullTowers) {
        super(player,match);
        this.numberOfFullTowers = numberOfFullTowers;
    }

    @Override
    public void checkWinCondition() {
        //win condition
    }
}
