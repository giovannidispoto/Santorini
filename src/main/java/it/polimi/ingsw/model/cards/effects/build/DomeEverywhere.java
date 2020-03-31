package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class DomeEverywhere extends BuildEffect {
    /**
     * @param currentMatch
     */
    public DomeEverywhere(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void buildBlock(Worker selectedWorker, int blockRow, int blockCol) throws RuntimeException {
        if(selectedWorker.getWorkerView()[blockRow][blockCol] == null)
            throw new RuntimeException("Illegal coordinates for worker");

        selectedWorker.getWorkerView()[blockRow][blockCol].getTower().addBlock(Block.DOME);
    }
}
