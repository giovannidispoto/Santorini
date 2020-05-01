package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.PlayerInterface;

import java.util.List;

/**
 * GetPlayersCommand represent getPlayers action returned by server
 */
public class GetPlayersCommand implements Command {
    private final List<PlayerInterface> players;

    /**
     * Create command
     * @param players players in game
     */
    public GetPlayersCommand(List<PlayerInterface> players) {
        this.players = players;
    }

    /*
     * Execute command
     */
    @Override
    public void execute(ClientController clientController) {
        clientController.setPlayers(this.players);

        for(PlayerInterface player : this.players){
            //Find player nickname same as that of this client
            if(player.getPlayerNickname().equals(clientController.getPlayerNickname())){
                //set the color assigned by the server
                clientController.setPlayerColor(player.getColor());
            }
        }
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockGetPlayers){
            clientController.lockManager.lockGetPlayers.setUsed();
            clientController.lockManager.lockGetPlayers.notify();
        }
    }
}
