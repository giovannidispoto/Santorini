package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;
import java.util.List;

public abstract class MoveEffect extends Turn {

    /**
     * Class Constructor
     */
    public MoveEffect() {
        super();
    }



    @Override
    public void checkLocalCondition(Worker selectedWorker) {
        if(reachedLevel3) {
            currentMatch.declareWinner(selectedWorker.getOwnerWorker()); //debug
        }
    }

    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol] == null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
        blocksLeft--;
    }
}
