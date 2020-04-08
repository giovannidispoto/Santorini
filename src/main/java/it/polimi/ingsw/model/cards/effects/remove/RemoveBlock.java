package it.polimi.ingsw.model.cards.effects.remove;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

public class RemoveBlock extends BasicRemove {
    /**
     * Class Constructor
     */
    public RemoveBlock() {
        super();
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
