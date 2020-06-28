package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.server.network.messagesInterfaces.CellInterface;

/**
 * Interface used for observe Battlefield change.
 * The update notify client about new configuration of battlefield.
 */
public interface ObserverBattlefield {
    /**
     * Update from Battlefield
     * @param cellInterfaces matrix
     */
    public void update(CellInterface[][] cellInterfaces);
}
