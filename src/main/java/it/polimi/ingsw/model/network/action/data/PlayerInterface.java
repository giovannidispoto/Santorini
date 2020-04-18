package it.polimi.ingsw.model.network.action.data;

import it.polimi.ingsw.model.Color;

import java.time.LocalDate;
import java.util.StringTokenizer;

public class PlayerInterface {
    private String playerNickname;
    private String date;
    private Color color;
    private String card;

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
