package it.polimi.ingsw.server.observers;

/**
 *  Interface used for observe WorkerView change.
 *  The update notify client about new WorkerView of the selected worker
 */
public interface ObserverWorkerView {
    /**
     * Update workerView as array of boolean
     * @param workerView workerView
     */
    public void update(boolean[][] workerView);
}
