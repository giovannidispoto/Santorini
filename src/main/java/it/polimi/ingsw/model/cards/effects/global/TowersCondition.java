package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

/**
 * TowersCondition class represents a global effect
 */
public class TowersCondition extends GlobalWinCondition {

    private final int numberOfFullTowers;

    /**
     * Class constructor
     * @param player is the player with this effect
     * @param match is the current match where there is the player
     * @param numberOfFullTowers is the number of Towers for the WinCondition
     */
    public TowersCondition(Player player, Match match, int numberOfFullTowers) {
        super(player,match);
        this.numberOfFullTowers = numberOfFullTowers;
    }

    /**
     * Count FullTowers presents on the battlefield
     */
    private int countFullTowers(){
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        int fullTowersCounter = 0;

        //check full towers presents in battlefield
        for(int i = 0; i < Battlefield.N_ROWS; i++) {
            for (int j = 0; j < Battlefield.N_COLUMNS; j++) {
                //check only possibles valid movements (by selected worker)
                if (battlefield.getCell(i, j).getTower().getHeight() == 4){
                    fullTowersCounter++;
                }
            }
        }
        return fullTowersCounter;
    }

    /**
     * Player with this effect win if there are a certain value of full towers built on the battlefield
     */
    @Override
    public void checkWinCondition() {

        //win condition
        if(countFullTowers() >= numberOfFullTowers){
            super.callWinner();
        }
    }

}
