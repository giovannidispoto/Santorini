package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.List;

public class SetPlayerCardCommand implements Command {
    List<String> cards;

    @Override
    public void execute(ClientController clientController) {
        clientController.setGodCards(cards);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockSetPlayerCard){
            clientController.lockManager.lockSetPlayerCard.notify();
            clientController.lockManager.lockSetPlayerCard.setUsed();
        }
    }
}
