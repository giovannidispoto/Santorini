package it.polimi.ingsw.client.controller;

public class LockObject {
    boolean state = false;

    public boolean isState() {
        return state;
    }

    public void setUsed() {
        this.state = true;
    }

}
