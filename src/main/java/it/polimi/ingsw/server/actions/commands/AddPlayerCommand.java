package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

/**
 * AddPlayerCommand class represent addPlayer action from the client
 */
public class AddPlayerCommand implements Command {

    private String playerNickname;
    private int lobbySize;
    private boolean lobbyState;
    private boolean validNick;
    private boolean fullLobby;

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
            fullLobby = controller.isFullLobby();

            if(validNick && lobbyState && !fullLobby){
                controller.registerHandler(playerNickname,handler);
                controller.addNewPlayer(playerNickname, lobbySize);
            }

            handler.responseQueue(new Gson().toJson(new BasicMessageResponse("addPlayerResponse", this)));
            handler.sendMessageQueue();
    }
}
