package it.polimi.ingsw.server.actions.data;

import it.polimi.ingsw.model.cards.Deck;

/**
 *
 */
public class DeckResponse {
    private Deck deck;

    public DeckResponse(Deck deck){
        this.deck = deck;
    }
}
