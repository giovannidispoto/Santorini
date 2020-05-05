package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

public class SetPickedCardsCommand implements Command {
    String playerNickname;

    public SetPickedCardsCommand(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    @Override
    public void execute(ClientController clientController) {
        //Set God Player
        clientController.setGodPlayer(this.playerNickname);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitSetPickedCards){
            clientController.waitManager.waitSetPickedCards.setUsed();
            clientController.waitManager.waitSetPickedCards.notify();
        }
    }
}
