package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class SkipStepCommand implements Command{

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.skipStep();
    }
}
