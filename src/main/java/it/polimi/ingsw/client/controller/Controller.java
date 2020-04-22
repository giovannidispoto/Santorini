package it.polimi.ingsw.client.controller;

import java.util.*;

import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.network.actions.data.PlayerInterface;


/**
 * Controller Class
 */
public class Controller {
    private Step turn;
    private List<PlayerInterface> players;
    private List<DivinityCard> cards;

    /**
     * Controller Constructor
     */
    public Controller(){
        this.players = new ArrayList<>();
        this.cards = new ArrayList<>();
    }


    /**
     * Gets players in match
     * @return players
     */
    public List<PlayerInterface> getPlayers(){
        return players;
    }

    /**
     * Sets players in match
     * @param players players in match
     */
    public void setPlayers(List<PlayerInterface> players){
        this.players = players;
    }
}
