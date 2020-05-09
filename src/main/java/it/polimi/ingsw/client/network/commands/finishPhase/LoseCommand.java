package it.polimi.ingsw.client.network.commands.finishPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.network.commands.Command;

public class LoseCommand implements Command {
    @Override
    public void execute(ClientController clientController) {
        clientController.setGameExceptionMessage(ExceptionMessages.loseMessage);
        clientController.interruptNormalExecution();
    }
}
