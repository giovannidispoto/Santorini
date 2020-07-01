package it.polimi.ingsw.server.network.messagesInterfaces;

import java.util.List;

/**
 * SetPlayerCardRequest class represent format of request sent to client
 */
public class SetPlayerCardRequest {
    private List<String> cards;

    /**
     * Create new request
     * @param cards cards of the deck
     */
    public SetPlayerCardRequest(List<String> cards){
        this.cards = cards;
    }
}
