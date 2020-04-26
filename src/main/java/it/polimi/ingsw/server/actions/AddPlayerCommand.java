package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.ClientHandler;

/**
 * AddPlayerCommand class represent addPlayer action from the client
 */
public class AddPlayerCommand implements Command {

    private String playerNickname;
    private int lobbySize;
    private boolean result;

    /**
     * Create command
     * @param playerNickname player
     * @param lobbySize lobbySize
     */
    public AddPlayerCommand(String playerNickname, int lobbySize) {
        this.playerNickname = playerNickname;
        this.lobbySize = lobbySize;
        this.result = false;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    public void execute(Controller controller, ClientHandler handler) {
            controller.registerHandler(playerNickname, handler);
            controller.addNewPlayer(playerNickname, lobbySize);
            System.out.println("Added new player: " + playerNickname);
            setResult(true);
    }

    /**
     * Set result of operation
     * @param result result
     */
    public void setResult(boolean result){
        this.result = result;
    }

    /**
     * Gets result of operation
     * @return result
     */
    public boolean getResult(){
        return this.result;
    }
}
