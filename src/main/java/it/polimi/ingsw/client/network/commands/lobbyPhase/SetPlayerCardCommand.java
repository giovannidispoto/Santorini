package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

import java.util.List;

public class SetPlayerCardCommand implements Command {
    List<String> cards;

    @Override
    public void execute(ClientController clientController) {
        clientController.setGodCards(cards);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitSetPlayerCard){
            clientController.waitManager.waitSetPlayerCard.setUsed();
            clientController.waitManager.waitSetPlayerCard.notify();
        }
    }
}
