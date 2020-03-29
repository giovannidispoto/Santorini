package it.polimi.ingsw.model.cards.effetcs.win;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class JumpEffect extends WinEffect {
    /**
     * @param currentMatch
     */
    public JumpEffect(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void checkLocalCondition(Worker selectedWorker) {

    }
}
