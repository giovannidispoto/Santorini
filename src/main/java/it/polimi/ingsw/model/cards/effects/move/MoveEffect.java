package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

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
            //currentMatch.declareWinner(); controller method needed
        }
    }

    @Override
    public void buildBlock(Worker selectedWorker, int blockRow, int blockCol) {
        Battlefield.getBattlefieldInstance().getTower(blockRow,blockCol).addNextBlock();
    }
}
