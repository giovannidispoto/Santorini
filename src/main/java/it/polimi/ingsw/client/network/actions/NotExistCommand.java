package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class NotExistCommand implements Command {
    @Override
    public void execute(ClientController clientController) {
        System.out.println("ServerMessage generate Error - ClientCommand does not exist");
        //TODO: debug
    }
}
