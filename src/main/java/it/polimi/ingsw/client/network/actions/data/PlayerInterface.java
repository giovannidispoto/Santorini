package it.polimi.ingsw.client.network.actions.data;

import it.polimi.ingsw.client.clientModel.basic.Color;

import java.time.LocalDate;
import java.util.StringTokenizer;

/**
 * PlayerInterface class represent the interface for the player used for communication with the client
 */
public class PlayerInterface {
    private String playerNickname;
    private String date;
    private Color color;
    private String card;

    /**
     * Create new Player
     * @param playerNickname player nickname
     * @param date date of birth
     * @param color color
     * @param card card
     */
    public PlayerInterface(String playerNickname, String date, Color color, String card) {
        this.playerNickname = playerNickname;
        this.color = color;
        this.card = card;
        this.date = date;
    }

    public LocalDate getDate() {
        StringTokenizer st = new StringTokenizer(date,"-");
        return LocalDate.of(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public Color getColor() {
        return color;
    }

    public String getCard() {
        return card;
    }

}
