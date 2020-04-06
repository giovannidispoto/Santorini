package it.polimi.ingsw.model;

public abstract class GlobalWinCondition {
    private Player subject;
    private Match match;

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
