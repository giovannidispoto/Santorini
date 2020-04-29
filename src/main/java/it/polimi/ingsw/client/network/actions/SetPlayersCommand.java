package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.PlayerInterface;

import java.util.List;

/**
 * SetPlayersCommand represent setPlayers action returned by server
 */
public class SetPlayersCommand implements Command {
    private final List<PlayerInterface> players;

    /**
     * Create command
     * @param players players in game
     */
    public SetPlayersCommand(List<PlayerInterface> players) {
        this.players = players;
    }

    /*
     * Execute command
     */
    @Override
    public void execute(ClientController clientController) {
        clientController.setPlayers(this.players);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockSetPlayers){
            clientController.lockManager.lockSetPlayers.setUsed();
            clientController.lockManager.lockSetPlayers.notify();
        }
    }
}
