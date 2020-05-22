package it.polimi.ingsw.client.network.commands.finishPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.network.commands.Command;

import java.util.Objects;

public class ServerErrorCommand implements Command {
    String errorMessage;

    public ServerErrorCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(ClientController clientController) {
        clientController.setGameExceptionMessage(Objects.requireNonNullElse(this.errorMessage, ExceptionMessages.defaultServerError));
        clientController.setGameState(GameState.ERROR);
        clientController.interruptNormalExecution();
    }
}
