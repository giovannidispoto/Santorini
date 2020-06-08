package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

import java.util.List;

/**
 * Class that manages the command: SetPlayerCard
 */
public class SetPlayerCardCommand implements Command {
    List<String> cards;

    @Override
    public void execute(ClientController clientController) {
        clientController.setGodCards(cards);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitSetPlayerCard){
            WaitManager.waitSetPlayerCard.setUsed();
            WaitManager.waitSetPlayerCard.notify();
        }
    }
}
