package it.polimi.ingsw.client.network.commands.finishPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

/**
 * Class that manages the command: Lose
 */
public class LoseCommand implements Command {
    @Override
    public void execute(ClientController clientController) {
        clientController.setGameExceptionMessage(ExceptionMessages.loseMessage, GameState.FINISH, true);

        synchronized (WaitManager.waitPlayStepResponse){
            /*wake up*/
            WaitManager.waitPlayStepResponse.notify();
        }
    }
}
