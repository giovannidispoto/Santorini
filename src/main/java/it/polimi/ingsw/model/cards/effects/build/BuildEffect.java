package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 * The BuildEffect card represent class of cards that have a build effect, assuming that the move is basic according to rules of the game
 */
public abstract class BuildEffect extends Turn {

    /**
     * @param currentMatch
     */
    public BuildEffect(Match currentMatch) {
        super(currentMatch);
    }

    public void moveWorker(Worker selectedWorker, int newRow, int newCol){}


    public void checkLocalCondition(Worker currentWorker){}
}
