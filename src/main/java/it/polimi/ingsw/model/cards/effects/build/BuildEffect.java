package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 * The BuildEffect card represent class of cards that have a build effect, assuming that the move is basic according to rules of the game
 */
public abstract class BuildEffect extends Turn {

    /**
     * Class Constructor
     */
    public BuildEffect() {
        super();
    }

    public void moveWorker(Worker selectedWorker, int newRow, int newCol){
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;
        //Set Worker Build Matrix - Basic building: Banned Cells = workers, Domes
        selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                && !(cell.getTower().getLastBlock() == Block.DOME)));

        movesLeft--;
    }


    public void checkLocalCondition(Worker currentWorker){
        if(reachedLevel3)
            currentMatch.declareWinner(currentWorker.getOwnerWorker()); //debug
    }


}
