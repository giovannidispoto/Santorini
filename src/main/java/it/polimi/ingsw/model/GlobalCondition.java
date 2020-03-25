package it.polimi.ingsw.model;

public abstract class GlobalCondition {
    private Player subject;
    private Match match;

    public GlobalCondition(Player subject, Match match){
        this.subject = subject;
        this.match = match;
    }
    public void callWinner(){
        //code
    }

    public abstract void checkWinCondition();
}
