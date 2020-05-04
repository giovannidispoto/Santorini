package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class NotExistCommand implements Command {
    private final String message;

    public NotExistCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(ClientController clientController) {
        if(clientController.debug) {
            System.out.println("\u001B[31m"+"ServerMessage generate Error - ClientCommand does not exist -  Server Sent:  " + this.message+"\u001B[0m");
        }
        //TODO: debug
    }
}
