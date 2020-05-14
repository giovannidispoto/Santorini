package it.polimi.ingsw.client.network.commands.allPhases;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

public class PingCommand implements Command {
    @Override
    public void execute(ClientController clientController) {
        clientController.sendPingResponse();
        clientController.resetPingTimer();
    }
}
