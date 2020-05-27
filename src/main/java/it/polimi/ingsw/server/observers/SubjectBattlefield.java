package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.server.observers.ObserverBattlefield;

/**
 * Observer interface used for Battlefield
 * */
public interface SubjectBattlefield {
    public void attach(ObserverBattlefield o);
    public void detach(ObserverBattlefield o);
    public void notifyUpdate();
}
