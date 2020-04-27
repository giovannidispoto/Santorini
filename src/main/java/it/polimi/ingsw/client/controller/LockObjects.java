package it.polimi.ingsw.client.controller;

public class LockObjects {
    public final Object lockGetPlayers;
    public final Object lockAddPlayer;
    public final Object lockSetPickedCards;
    public final Object lockGetDeck;
    public final Object lockSetPlayerCard;

    public LockObjects() {
        this.lockGetPlayers =   new Object();
        this.lockAddPlayer  =   new Object();
        this.lockSetPickedCards =   new Object();
        this.lockGetDeck    =   new Object();
        this.lockSetPlayerCard  =   new Object();
    }

    public boolean setWait(Object object){
        try {
            object.wait();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
