package it.polimi.ingsw.server.network.messagesInterfaces;

import it.polimi.ingsw.model.Color;

/**
 * PlayerInterface class represent the interface for the player used for communication with the client
 */
public class PlayerInterface {
    private String playerNickname;
    private Color color;
    private String card;

    /**
     * Create new Player
     * @param playerNickname player nickname
     * @param color color
     * @param card card
     */
    public PlayerInterface(String playerNickname, Color color, String card) {
        this.playerNickname = playerNickname;
        this.color = color;
        this.card = card.toUpperCase();
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public Color getColor() {
        return color;
    }

    public String getCard(){
        return card;
    }


}