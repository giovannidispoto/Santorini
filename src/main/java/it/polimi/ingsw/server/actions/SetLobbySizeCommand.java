package it.polimi.ingsw.server.actions;


import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * SetLobbySize represent setLobbySize action from the client
 */
public class SetLobbySizeCommand implements Command {
    private String playerNickname;
    private int lobbySize;
    private boolean result;

    /**
     *
     * @param playerNickname
     * @param lobbySize
     */
    public SetLobbySizeCommand(String playerNickname, int lobbySize){
        this.playerNickname = playerNickname;
        this.lobbySize = lobbySize;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.setLobbySize(playerNickname,lobbySize);
        result = true;
    }

    public boolean getResult(){
        return result;
    }
}
