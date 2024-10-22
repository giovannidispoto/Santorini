package it.polimi.ingsw.model.cards.effects.basic;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.controller.Step;

import java.util.List;

/**
 * BlockUnder Effect
 */
public class BlockUnder extends BasicTurn {
    /**
     * Class Constructor
     * @param turnStructure is the list of the steps for the turn
     */
    public BlockUnder(List<Step> turnStructure) {
        super();
    }

    /**
     * This method returns a full Worker View Matrix
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     */
    @Override
    public Cell[][] generateBuildingMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Deny building on cells with workers and domes
        Cell[][] underBlockMatrix = battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                && !(cell.getTower().getLastBlock() == Block.DOME));
        //Control if you can allow worker to build under his self, if is on a level3 tower can't
        if (battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() < 3)
        {
            underBlockMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=Battlefield.getBattlefieldInstance().getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker());
        }

        return underBlockMatrix;
    }


}
