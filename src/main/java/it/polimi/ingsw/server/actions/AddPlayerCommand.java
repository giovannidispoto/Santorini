package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.actions.data.PlayerInterface;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.time.LocalDate;

/**
 * AddPlayerCommand class represent addNewPlayer action from the client
 */
public class AddPlayerCommand implements Command {

    private String playerNickname;
    private String date;
    private Color color;

    /**
     * Create command
     * @param playerNickname player
     * @param date date of birth
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
