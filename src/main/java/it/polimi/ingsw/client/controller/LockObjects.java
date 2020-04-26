package it.polimi.ingsw.client.controller;

public class LockObjects {
    public final Object lockGetPlayers, lockAddPlayer, lockLobbyReady;

    public LockObjects() {
        this.lockGetPlayers = new Object();
        this.lockAddPlayer = new Object();
        this.lockLobbyReady = new Object();
    }

    public Boolean setWait(Object object){
        try {
            object.wait();
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }
}
