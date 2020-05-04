package it.polimi.ingsw.server;

import it.polimi.ingsw.server.actions.data.CellInterface;

/**
 * Interface used in Observer Pattern
 */
public interface ObserverBattlefield {
    /**
     * Update from Battlefield or workerView, both represented by array of CellInterface
     * @param cellInterfaces matrix
     * @param message who update
     */
    public void update(CellInterface[][] cellInterfaces, Message message);
}
