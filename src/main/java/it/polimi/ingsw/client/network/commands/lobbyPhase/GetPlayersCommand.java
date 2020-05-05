package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;
import it.polimi.ingsw.client.network.data.dataInterfaces.PlayerInterface;

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
        synchronized (clientController.waitManager.waitGetPlayers){
            clientController.waitManager.waitGetPlayers.setUsed();
            clientController.waitManager.waitGetPlayers.notify();
        }
    }
}
