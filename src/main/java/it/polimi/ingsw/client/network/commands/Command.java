package it.polimi.ingsw.client.network.commands;

import it.polimi.ingsw.client.controller.ClientController;

/**
 * Interface used in Command Pattern
 */
public interface Command {
    /**
     * Execute command<br>
     * The command has the task of taking the data arrived from the server (manipulating them if necessary)
     * and storing them in the clientController, where they will be available to all the client
     * @param clientController context
     */
    void execute(ClientController clientController);
}
