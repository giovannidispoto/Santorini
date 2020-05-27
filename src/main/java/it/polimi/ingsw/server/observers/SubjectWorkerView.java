package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.server.observers.ObserverWorkerView;

/** Observer interface specialized on WorkerView*/
public interface SubjectWorkerView {
    public void attach(ObserverWorkerView o);
    public void detach(ObserverWorkerView o);
    public void detachAll();
    public void notifyUpdate();
}
