package it.polimi.ingsw.server.observers;

/**
 * Interface used in Observer Pattern
 */
public interface ObserverWorkerView {
    /**
     * Update workerView as array of boolean
     * @param workerView workerView
     */
    public void update(boolean[][] workerView);
}
