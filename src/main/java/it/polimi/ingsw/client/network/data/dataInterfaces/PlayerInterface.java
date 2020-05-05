package it.polimi.ingsw.client.network.data.dataInterfaces;

import it.polimi.ingsw.client.clientModel.basic.Color;

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
     */
    public PlayerInterface(String playerNickname, Color color) {
        this.playerNickname = playerNickname;
        this.color = color;
    }

    //Getters & Setters
    public String getPlayerNickname() {
        return playerNickname;
    }
    public Color getColor() {
        return color;
    }
    public String getCard() {
        return card;
    }
    public void setCard(String card) {
        this.card = card;
    }
    public void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }
    public void setColor(Color color) {
        this.color = color;
    }

}
