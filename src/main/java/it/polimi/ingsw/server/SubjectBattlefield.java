package it.polimi.ingsw.server;

public interface SubjectBattlefield {
    public void attach(ObserverBattlefield o);
    public void detach(ObserverBattlefield o);
    public void notifyUpdate();
}
