package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class LobbyReadyCommand implements Command {
    int lobbySize;

    public LobbyReadyCommand (int lobbySize){
        this.lobbySize = lobbySize;
    }

    @Override
    public void execute(ClientController clientController) {
        if(clientController.getCurrentLobbySize() == this.lobbySize){
            //Awakens who was waiting Server Response
            synchronized (clientController.lockObjects.lockLobbyReady){
                clientController.lockObjects.lockLobbyReady.notify();
            }
        }
        else{
            //TODO: Method-General Error
            //Launch General Error
        }
    }
}
