package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.network.commands.Command;

public class GetDeckCommand implements Command {
    Deck deck;
    boolean result;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCardsDeck(this.deck);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitGetDeck){
            clientController.waitManager.waitGetDeck.setUsed();
            clientController.waitManager.waitGetDeck.notify();
        }
    }
}
