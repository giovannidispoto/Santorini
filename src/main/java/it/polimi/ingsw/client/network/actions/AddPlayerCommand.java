package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class AddPlayerCommand implements Command {
    Boolean validNick, lobbyState;
    int lobbySize;

    public AddPlayerCommand (Boolean validNick, Boolean lobbyState, int lobbySize){
        this.validNick = validNick;
        this.lobbyState = lobbyState;
        this.lobbySize = lobbySize;
    }

    @Override
    public void execute(ClientController clientController) {
        clientController.setValidNick(this.validNick);
        clientController.setLobbyState(this.lobbyState);
        clientController.setCurrentLobbySize(lobbySize);

        //Awakens who was waiting Server Response
        synchronized (clientController.lockObjects.lockAddPlayer){
            clientController.lockObjects.lockAddPlayer.notify();
        }
    }
}
