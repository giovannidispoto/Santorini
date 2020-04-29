package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.controller.ClientController;

public class GetCardsInGameCommand implements Command {
    Deck deck;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCardsDeck(this.deck);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockGetCardsInGame){
            clientController.lockManager.lockGetCardsInGame.setUsed();
            clientController.lockManager.lockGetCardsInGame.notify();
        }
    }
}
