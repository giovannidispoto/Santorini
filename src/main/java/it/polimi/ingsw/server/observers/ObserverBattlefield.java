package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.server.actions.data.CellInterface;

/**
 * Interface used in Observer Pattern
 */
public interface ObserverBattlefield {
    /**
     * Update from Battlefield
     * @param cellInterfaces matrix
     */
    public void update(CellInterface[][] cellInterfaces);
}
