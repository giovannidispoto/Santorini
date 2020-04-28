package it.polimi.ingsw.server.actions.data;

import it.polimi.ingsw.model.cards.DivinityCard;

import java.util.List;

public class SetPlayerCardRequest {
    private List<DivinityCard> cards;

    public SetPlayerCardRequest(List<DivinityCard> cards){
        this.cards = cards;
    }
}
