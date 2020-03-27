package it.polimi.ingsw.model;

public abstract class GlobalWinCondition {
    private Player subject;
    private Match match;

    public GlobalWinCondition(Player subject, Match match){
        this.subject = subject;
        this.match = match;
    }
    public void callWinner(){
        //code
    }

    public abstract void checkWinCondition();
}
