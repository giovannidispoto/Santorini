package it.polimi.ingsw.server;

/**
 * Observer interface used for Battlefield
 * */
public interface SubjectBattlefield {
    public void attach(ObserverBattlefield o);
    public void detach(ObserverBattlefield o);
    public void notifyUpdate();
}
