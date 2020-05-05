package it.polimi.ingsw.client.network.commands;

import it.polimi.ingsw.client.controller.ClientController;

/**
 * Interface used in Command Pattern
 */
public interface Command {
    /**
     * Execute command
     * @param clientController context
     */
    void execute(ClientController clientController);
}
