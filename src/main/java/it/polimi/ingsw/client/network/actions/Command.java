package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.Controller;

/**
 * Interface used in Command Pattern
 */
public interface Command {
    /**
     * Execute command
     * @param controller context
     */
    void execute(Controller controller);
}
