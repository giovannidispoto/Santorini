package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

/**
 * Class that manages the command: GetDeck
 */
public class GetDeckCommand implements Command {
    Deck deck;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCardsDeck(this.deck);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitGetDeck){
            WaitManager.waitGetDeck.setUsed();
            WaitManager.waitGetDeck.notify();
        }
    }
}
