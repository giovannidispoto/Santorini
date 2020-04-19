package it.polimi.ingsw.model;

import it.polimi.ingsw.model.network.action.data.CellInterface;

/**
 * Interface used in Observer Pattern
 */
public interface Observer {
    /**
     * Update from Battlefield or workerView, both represented by array of CellInterface
     * @param cellInterfaces matrix
     * @param message who update
     */
    public void update(CellInterface[][] cellInterfaces, Match.Message message);
}
