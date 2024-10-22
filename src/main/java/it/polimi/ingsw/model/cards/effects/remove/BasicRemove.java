package it.polimi.ingsw.model.cards.effects.remove;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 * BasicRemove class
 */
public abstract class BasicRemove extends Turn {
    /**
     * Class Constructor
     */
    public BasicRemove() {
        super();
    }

    /**
     * This method describes a basic move action
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     * @param newRow is the x coordinate of the destination cell
     * @param newCol is the y coordinate of the destination cell
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol]==null)
            throw new RuntimeException("Unexpected Error!");

        int lvl_b = Battlefield.getBattlefieldInstance().getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = Battlefield.getBattlefieldInstance().getCell(newRow, newCol).getTower().getHeight();
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;

        movesLeft--;
    }

    /**
     * This method describes a basic build action
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

    /**
     * This method checks the local win condition
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     */
    public void checkLocalCondition(Worker selectedWorker){
        if(reachedLevel3) {
            currentMatch.declareWinner(selectedWorker.getOwnerWorker()); //debug
        }
    }

    /**
     * Abstract methods that are specialized by the sons of this class
     */
    public abstract Cell[][] generateRemoveMatrix(Worker selectedWorker);
}
