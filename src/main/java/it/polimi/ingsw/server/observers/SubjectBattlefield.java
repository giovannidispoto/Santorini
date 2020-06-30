package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.server.observers.ObserverBattlefield;

/**
 * Observer interface used for Battlefield
 * */
public interface SubjectBattlefield {
    /**
     * Attach observer to subject
     * @param o observer
     */
    public void attach(ObserverBattlefield o);

    /**
     * Detech observer from subject
     * @param o observer
     */
    public void detach(ObserverBattlefield o);

    /**
     * Notify Update to Observer
     */
    public void notifyUpdate();
}
