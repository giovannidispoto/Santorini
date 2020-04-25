package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.actions.data.PlayerInterface;
import it.polimi.ingsw.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * GetPlayersCommand represent getPlayers action from the client
 */
public class GetPlayersCommand implements Command {
    private boolean result;
    private List<PlayerInterface> players;


    public GetPlayersCommand(){
        players = new ArrayList<>();
    }

    @Override
    /*
     * Execute command
     * @param controller context
     * @param handler context
     */
    public void execute(Controller controller, ClientHandler handler) {
        List<Player> playersList = controller.getPlayers();
        for(Player p : playersList)
           players.add(new PlayerInterface(p.getPlayerNickname(),p.getPlayerColor()));
        result = true;
    }

    public boolean getResult(){
        return result;
    }
}
