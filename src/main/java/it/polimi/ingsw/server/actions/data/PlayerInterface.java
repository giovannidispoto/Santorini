package it.polimi.ingsw.server.actions.data;

import it.polimi.ingsw.model.Color;

import java.time.LocalDate;
import java.util.StringTokenizer;

/**
 * PlayerInterface class represent the interface for the player used for communication with the client
 */
public class PlayerInterface {
    private String playerNickname;
    private Color color;

    /**
     * Create new Player
     * @param playerNickname player nickname
     * @param color color
     */
    public PlayerInterface(String playerNickname, Color color) {
        this.playerNickname = playerNickname;
        this.color = color;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public Color getColor() {
        return color;
    }


}
