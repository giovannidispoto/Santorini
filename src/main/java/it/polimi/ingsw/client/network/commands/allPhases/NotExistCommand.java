package it.polimi.ingsw.client.network.commands.allPhases;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

public class NotExistCommand implements Command {
    private final String message;

    public NotExistCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(ClientController clientController) {
        clientController.loggerIO.severe("ServerMessage generate Error - ClientCommand does not exist but syntax is OK -  Server Sent:  " + this.message + "\n");
    }
}
