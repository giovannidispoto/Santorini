package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

/**
 * Class that manages the command: ActualPlayer
 */
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
        synchronized (WaitManager.waitAddPlayer){
            WaitManager.waitAddPlayer.setUsed();
            WaitManager.waitAddPlayer.notify();
        }
    }
}
