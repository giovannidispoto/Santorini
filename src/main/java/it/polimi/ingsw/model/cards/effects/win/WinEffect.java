package it.polimi.ingsw.model.cards.effects.win;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

public abstract class WinEffect extends Turn {
    /**
     * @param currentMatch
     */
   /* public WinEffect(Match currentMatch) {
        super(currentMatch);
    }*/

    /**
     * This method describes a basic move action
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     * @param newRow is the x coordinate of the destination cell
     * @param newCol is the y coordinate of the destination cell
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        int lvl_b = Battlefield.getBattlefieldInstance().getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = Battlefield.getBattlefieldInstance().getCell(newRow, newCol).getTower().getHeight();
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;
    }

    /**
     * This method describes a basic move action
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     * @param newBlockRow is the newBlockRow coordinate of the destination cell
     * @param newBlockCol is the newBlockCol coordinate of the destination cell
     */
    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
    }

}
