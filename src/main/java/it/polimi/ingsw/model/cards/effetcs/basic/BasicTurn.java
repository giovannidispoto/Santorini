package it.polimi.ingsw.model.cards.effetcs.basic;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

public class BasicTurn extends Turn {
    /**
     * @param currentMatch
     */
    public BasicTurn(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        selectedWorker.changeWorkerPosition(newRow,newCol);
    }

    @Override
    public void buildBlock(Worker selectedWorker, int x, int y) {
       Battlefield.getBattelfieldInstance().getTower(x,y).addNextBlock();
    }

    public void checkLocalCondition(){

    }


}
