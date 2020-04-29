package it.polimi.ingsw.client.controller;

public class LockManager {
    public final LockObject lockGetPlayers;
    public final LockObject lockAddPlayer;
    public final LockObject lockSetPickedCards;
    public final LockObject lockGetDeck;
    public final LockObject lockSetPlayerCard;

    public LockManager() {
        this.lockGetPlayers =   new LockObject();
        this.lockAddPlayer  =   new LockObject();
        this.lockSetPickedCards =   new LockObject();
        this.lockGetDeck    =   new LockObject();
        this.lockSetPlayerCard  =   new LockObject();
    }

    public boolean setWait(LockObject object){
        //If the unlocking operation has already been performed
        if(object.isState()){
            return true;
        }
        //If the unlocking operation has not already been performed -> WAIT
        try {
            //TODO: Thinking about it: (Gorlenah)
            //object.setFalse?
            object.wait();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
