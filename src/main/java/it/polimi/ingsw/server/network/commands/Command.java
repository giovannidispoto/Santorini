package it.polimi.ingsw.server.network.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.network.ClientHandler;

/**
 * Interface used in Command Pattern
 */
public interface Command {
    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    public void execute(Controller controller, ClientHandler handler);
}
