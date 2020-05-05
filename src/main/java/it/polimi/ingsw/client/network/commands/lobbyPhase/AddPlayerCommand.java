package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

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
        synchronized (clientController.waitManager.waitAddPlayer){
            clientController.waitManager.waitAddPlayer.setUsed();
            clientController.waitManager.waitAddPlayer.notify();
        }
    }
}
