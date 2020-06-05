package it.polimi.ingsw.model.cards.effects.basic;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.controller.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * CantLevelUp Effect
 */
public class CantLevelUp extends BasicTurn {

    /**
     * Class Constructor
     */
    public CantLevelUp(List<Step> turnStructure) {
        super.turnStructure = new ArrayList<>(turnStructure);
    }

    /**
     * Create a variant of movementMatrix that contains cell with tower less or equal
     * @param selectedWorker worker
     * @return movementMatrix
     */
    @Override
    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Basic movement: Banned Cells = workers, higher than the worker (same level or down), Dome
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() >= cell.getTower().getHeight()
                && !(cell.getTower().getLastBlock() == Block.DOME));
    }
}
