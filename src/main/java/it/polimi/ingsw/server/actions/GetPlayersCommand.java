package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.actions.data.PlayerInterface;
import it.polimi.ingsw.server.actions.data.PlayersInterface;
import it.polimi.ingsw.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * GetPlayersCommand represent getPlayers action from the client
 */
public class GetPlayersCommand implements Command {
    @Override

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    public void execute(Controller controller, ClientHandler handler) {
        List<Player> players = controller.getPlayers();
        List<PlayerInterface> playerInterfaces = new ArrayList<>();
        for(Player p : players)
            playerInterfaces.add(new PlayerInterface(p.getPlayerNickname(), p.getPlayerBirthday().toString(),p.getPlayerColor(),p.getPlayerCard().getCardName()));
        handler.response(new Gson().toJson(new PlayersInterface("getPlayers", playerInterfaces)));
    }
}
