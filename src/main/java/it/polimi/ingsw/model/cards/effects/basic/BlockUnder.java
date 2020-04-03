package it.polimi.ingsw.model.cards.effects.basic;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.cards.effects.build.BuildEffect;
import it.polimi.ingsw.model.cards.effects.move.MoveEffect;

public class BlockUnder extends BasicTurn {
    /**
     * Class Constructor
     */
    public BlockUnder() {
        super();
    }

    /**
     * This method returns a full Worker View Matrix
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     */
    @Override
    public Cell[][] generateBuildingMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        Cell[][] underBlockMatrix = battlefield.getWorkerView(selectedWorker);
        underBlockMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=Battlefield.getBattlefieldInstance().getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker());
        //return Battlefield.getBattlefieldInstance().getWorkerView(selectedWorker);
        return underBlockMatrix;
    }
}
