package it.polimi.ingsw.server.actions.data;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.cards.DivinityCard;

/**
 * PlayerInterface class represent the interface for the player used for communication with the client
 */
public class PlayerInterface {
    private String playerNickname;
    private Color color;
    private DivinityCard card;

    /**
     * Create new Player
     * @param playerNickname player nickname
     * @param color color
     */
    public PlayerInterface(String playerNickname, Color color, DivinityCard card) {
        this.playerNickname = playerNickname;
        this.color = color;
        this.card = card;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public Color getColor() {
        return color;
    }

    public DivinityCard getCard(){
        return card;
    }


}
