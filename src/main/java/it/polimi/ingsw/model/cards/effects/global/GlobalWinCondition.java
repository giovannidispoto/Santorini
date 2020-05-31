package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

public abstract class GlobalWinCondition {
    private final Player subject;
    private final Match match;

    /**
     * Class Constructor
     */
    public GlobalWinCondition(Player subject, Match match){
        this.subject = subject;
        this.match = match;
    }

    /**
     * Reports to the current match that there is a winner
     */
    public void callWinner(){
        match.declareWinner(subject);
    }

    /**
     * Check if a win condition has been triggered (abstract)
     */
    public abstract void checkWinCondition();
}
