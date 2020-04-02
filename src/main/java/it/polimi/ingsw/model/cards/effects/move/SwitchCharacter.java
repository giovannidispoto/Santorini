package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class SwitchCharacter extends MoveEffect {
    /**
     * Class Constructor
     */
    public SwitchCharacter() {
        super();
    }

    /**
     * Allows you to switch your selected worker with an enemy one besides him during your movement phase
     * @param selectedWorker is the worker selected by the current player
     * @param newRow is the new x coordinate of the worker
     * @param newCol is the new y coordinate of the worker
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {

    }
}
