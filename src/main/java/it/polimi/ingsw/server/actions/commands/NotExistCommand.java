package it.polimi.ingsw.server.actions.commands;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**Command used for debug only*/

public class NotExistCommand implements Command {
    private final String message;

    public NotExistCommand(String message) {
        this.message = message;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        System.out.println("Command not found");
    }
}
