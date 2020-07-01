package it.polimi.ingsw.model.cards.effects.win;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 * WinEffect class
 */
public abstract class WinEffect extends Turn {
    /**
     * Class Constructor
     */
    public WinEffect() {
        super();
    }

    /**
     * Abstract methods that are specialized by the sons of this class
     */
    public abstract void moveWorker(Worker selectedWorker, int newRow, int newCol);

    /**
     * This method describes a basic move action
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     * @param newBlockRow is the newBlockRow coordinate of the destination cell
     * @param newBlockCol is the newBlockCol coordinate of the destination cell
     */
    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();

        blocksLeft--;
    }

}
