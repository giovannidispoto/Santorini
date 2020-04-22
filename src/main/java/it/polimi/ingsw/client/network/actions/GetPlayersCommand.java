package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.network.actions.data.PlayerInterface;

import java.util.List;

/**
 * GetPlayersCommand represent getPlayers action returned by server
 */
public class GetPlayersCommand implements Command {
    List<PlayerInterface> players;

    /**
     * Create command
     * @param players players in game
     */
    public GetPlayersCommand(List<PlayerInterface> players) {
        this.players = players;
    }


    /*
     * Execute command
     * @param controller context
     */
    @Override
    public void execute(Controller controller) {
        controller.setPlayers(players);
    }
}
