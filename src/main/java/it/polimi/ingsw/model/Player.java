package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;

import java.time.LocalDate;
import java.util.List;

/**
 * The Player class represent the player of the game
* */
public class Player {

    private final LocalDate playerBirthday;
    private final String playerNickname;
    private DivinityCard playerCard;
    private List<Worker> playerWorkers;
    private final Color playerColor;

    /**
     *
     * @param playerNickname
     * @param playerBirthday
     * @param playerColor
     */
    public Player(String playerNickname, LocalDate playerBirthday, Color playerColor ){
        this.playerNickname = playerNickname;
        this.playerBirthday = playerBirthday;
        this.playerColor = playerColor;
    }

    /**
     *
     * @param card
     */
    public void setPlayerCard(DivinityCard card){
        this.playerCard = card;
    }

    /**
     * Get color of player
     * @return color
    * */
    public Color getPlayerColor() {
        return playerColor;
    }


    /**
     *
     * @return
     */



}
