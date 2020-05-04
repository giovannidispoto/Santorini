package it.polimi.ingsw.server;

import it.polimi.ingsw.server.actions.data.CellInterface;

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
