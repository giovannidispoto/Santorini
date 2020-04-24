package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.ClientHandler;

/**
 * AddPlayerCommand class represent addPlayer action from the client
 */
public class AddPlayerCommand implements Command {

    private String playerNickname;
    private Color color;

    /**
     * Create command
     * @param playerNickname player
     * @param color color
     */
    public AddPlayerCommand(String playerNickname, Color color) {
        this.playerNickname = playerNickname;
        this.color = color;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    public void execute(Controller controller, ClientHandler handler) {
            controller.registerHandler(playerNickname, handler);
            controller.addNewPlayer(playerNickname, color);
            controller.addWorkers(playerNickname, handler);
            System.out.println("Added new player: " + playerNickname);
    }
}
