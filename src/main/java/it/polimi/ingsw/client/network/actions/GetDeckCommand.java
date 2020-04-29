package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.clientModel.basic.Deck;

public class GetDeckCommand implements Command {
    Deck deck;
    boolean result;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCardsDeck(this.deck);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockGetDeck){
            clientController.lockManager.lockGetDeck.setUsed();
            clientController.lockManager.lockGetDeck.notify();
        }
    }
}
