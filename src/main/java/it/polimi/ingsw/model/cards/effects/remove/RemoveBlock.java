package it.polimi.ingsw.model.cards.effects.remove;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class RemoveBlock extends BasicRemove {
    /**
     * Class Constructor
     */
    public RemoveBlock() {
        super();
        super.turnStructure = new ArrayList<>();
        super.turnStructure.add(Step.MOVE);
        super.turnStructure.add(Step.BUILD);
        super.turnStructure.add(Step.REMOVE);
        super.turnStructure.add(Step.END);
    }

    /**
     * Allows you to generate a RemoveMatrix without cells where there is a player a dome or ground
     * @param selectedWorker is the worker selected by the current player
     * @return return the RemoveMatrix
     */
    @Override
    public Cell[][] generateRemoveMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //WorkerView without cells where there is a player a dome or ground
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                && !(cell.getTower().getLastBlock() == Block.DOME)
                && !(cell.getTower().getHeight() == 0));
        //the player, now can do the remove correctly
    }

}
