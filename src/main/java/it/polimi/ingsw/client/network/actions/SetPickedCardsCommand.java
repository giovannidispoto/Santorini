package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class SetPickedCardsCommand implements Command{
    String playerNickname;

    public SetPickedCardsCommand(String playerNickname) {
        this.playerNickname = playerNickname;
    }


    @Override
    public void execute(ClientController clientController) {
        //Set God Player
        clientController.setGodPlayer(this.playerNickname);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockObjects.lockSetPickedCards){
            clientController.lockObjects.lockSetPickedCards.notify();
        }
    }
}
