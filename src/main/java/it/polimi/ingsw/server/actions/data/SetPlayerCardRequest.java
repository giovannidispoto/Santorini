package it.polimi.ingsw.server.actions.data;

import it.polimi.ingsw.model.cards.DivinityCard;

import java.util.List;

public class SetPlayerCardRequest {
    private List<String> cards;

    public SetPlayerCardRequest(List<String> cards){
        this.cards = cards;
    }
}
