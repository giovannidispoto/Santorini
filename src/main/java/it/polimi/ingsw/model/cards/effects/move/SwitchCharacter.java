package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class SwitchCharacter extends MoveEffect {
    /**
     * @param currentMatch
     */
    public SwitchCharacter(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {

    }


}
