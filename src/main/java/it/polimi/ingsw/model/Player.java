package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.DivinityCard;

import java.util.Date;
import java.util.List;

/**
 * The Player class represent the player of the game
* */
public class Player {

    private final Date playerBirthday;
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
    public Player(String playerNickname,Date playerBirthday, Color playerColor ){
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
     *
     * @return
     */
    public Worker chooseWorker(){
        return null;
    }


}
