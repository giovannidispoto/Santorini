package it.polimi.ingsw.server.network.messagesInterfaces;

import java.util.List;

/**Interface used for rending request to client*/
public class SetPlayerCardRequest {
    private List<String> cards;

    public SetPlayerCardRequest(List<String> cards){
        this.cards = cards;
    }
}
