package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.server.observers.ObserverWorkerView;

/**
 * Observer interface used for WorkerView
 * */
public interface SubjectWorkerView {
    /**
     * Attach observer to subject
     * @param o observer
     */
    public void attach(ObserverWorkerView o);
    /**
     * Detach observer to subject
     * @param o observer
     */
    public void detach(ObserverWorkerView o);

    /**
     * Detach all observer from the subject
     */
    public void detachAll();

    /**
     * Notify update
     */
    public void notifyUpdate();
}
