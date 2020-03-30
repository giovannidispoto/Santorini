package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

public abstract class MoveEffect extends Turn {

    /**
     * @param currentMatch
     */
    public MoveEffect(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void checkLocalCondition(Worker selectedWorker) {

    }

    @Override
    public void buildBlock(Worker selectedWorker, int blockRow, int blockCol) {

    }
}
