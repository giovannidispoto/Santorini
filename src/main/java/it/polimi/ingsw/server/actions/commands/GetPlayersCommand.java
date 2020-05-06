package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.PlayerInterface;
import it.polimi.ingsw.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * GetPlayersCommand represent getPlayers action from the client
 */
public class GetPlayersCommand implements Command {
    private List<PlayerInterface> players;


    public GetPlayersCommand(){
        players = new ArrayList<>();
    }


    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        List<Player> playersList = controller.getPlayers();
        for(Player p : playersList)
            players.add(new PlayerInterface(p.getPlayerNickname(),p.getPlayerColor(), p.getPlayerCard().getCardName()));
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("getPlayersResponse", this)));
        handler.sendMessageQueue();
    }
}
