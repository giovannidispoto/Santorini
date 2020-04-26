package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.ClientHandler;

/**
 * AddPlayerCommand class represent addPlayer action from the client
 */
public class AddPlayerCommand implements Command {

    private String playerNickname;
    private int lobbySize;
    private boolean lobbyState;
    private boolean validNick;

    /**
     * Create command
     * @param playerNickname player
     * @param lobbySize lobbySize
     */
    public AddPlayerCommand(String playerNickname, int lobbySize) {
        this.playerNickname = playerNickname;
        this.lobbySize = lobbySize;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    public void execute(Controller controller, ClientHandler handler) {
            validNick = controller.isValidNickame(playerNickname);
            lobbyState = controller.isValidLobby(lobbySize);

            if(validNick && lobbyState){
                controller.registerHandler(playerNickname,handler);
                controller.addNewPlayer(playerNickname, lobbySize);
            }

            handler.response(new Gson().toJson(new BasicMessageInterface("addPlayerResponse", this)));
    }
}
