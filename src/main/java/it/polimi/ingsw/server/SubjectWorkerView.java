package it.polimi.ingsw.server;

/** Observer interface specialized on WorkerView*/
public interface SubjectWorkerView {
    public void attach(ObserverWorkerView o);
    public void detach(ObserverWorkerView o);
    public void detachAll();
    public void notifyUpdate();
}
