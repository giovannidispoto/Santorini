package it.polimi.ingsw.server.actions.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/** Pong command from the client */
public class PongCommand implements Command {
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        handler.resetTimeout();
        handler.setTimer();
    }
}
