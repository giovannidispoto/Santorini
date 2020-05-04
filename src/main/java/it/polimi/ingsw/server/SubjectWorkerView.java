package it.polimi.ingsw.server;

public interface SubjectWorkerView {
    public void attach(ObserverWorkerView o);
    public void detach(ObserverWorkerView o);
    public void detachAll();
    public void notifyUpdate();
}
