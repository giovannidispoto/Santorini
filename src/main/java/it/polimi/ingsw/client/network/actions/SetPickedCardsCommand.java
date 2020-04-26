package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class SetPickedCardsCommand implements Command{
    String playerNickname;
    int lobbySize;

    public SetPickedCardsCommand(String playerNickname, int lobbySize) {
        this.playerNickname = playerNickname;
        this.lobbySize = lobbySize;
    }


    @Override
    public void execute(ClientController clientController) {
        //Set God Player
        clientController.setGodPlayer(this.playerNickname);
        //Check correct lobby
        if(clientController.getCurrentLobbySize() == this.lobbySize){
            //Awakens who was waiting Server Response
            synchronized (clientController.lockObjects.lockSetPickedCards){
                clientController.lockObjects.lockSetPickedCards.notify();
            }
        }
        else{
            //TODO: Method-General Error
            //Launch General Error
        }

    }
}
