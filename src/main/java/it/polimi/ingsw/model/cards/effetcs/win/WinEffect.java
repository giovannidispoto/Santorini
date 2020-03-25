package it.polimi.ingsw.model.cards.effetcs.win;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

public abstract class WinEffect extends Turn {
    /**
     * @param currentMatch
     */
    public WinEffect(Match currentMatch) {
        super(currentMatch);
    }

    public void moveWorker(Worker selectedWorker, int newRow, int newCol){

    }

    public void buildBlock(Worker selectedWorker, int blockRow, int blockCol){

    }


}
