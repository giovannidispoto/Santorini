package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

public class AddPlayerCommand implements Command {
    boolean validNick;
    boolean lobbyState;
    boolean fullLobby;
    int lobbySize;

    public AddPlayerCommand (boolean validNick, boolean lobbyState, int lobbySize, boolean fullLobby){
        this.validNick  =   validNick;
        this.lobbyState =   lobbyState;
        this.lobbySize  =   lobbySize;
        this.fullLobby  =   fullLobby;
    }

    @Override
    public void execute(ClientController clientController) {
        clientController.setValidNick(this.validNick);
        clientController.setLobbyState(this.lobbyState);
        clientController.setCurrentLobbySize(this.lobbySize);
        clientController.setFullLobby(this.fullLobby);

        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockAddPlayer){
            clientController.lockManager.lockAddPlayer.notify();
            clientController.lockManager.lockAddPlayer.setUsed();
        }
    }
}
