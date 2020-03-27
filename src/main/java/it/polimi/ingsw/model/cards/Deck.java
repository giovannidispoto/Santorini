package it.polimi.ingsw.model.cards;

import java.util.List;
import java.util.Optional;

/**
 * The Deck class model the effective deck composed by DivinityCards
 */
public class Deck {

    private List<DivinityCard> listOfCards;
    private Deck deckInstance = null;

    public Deck(List<DivinityCard> listOfCards){
       this.listOfCards = List.copyOf(listOfCards);
    }

    public DivinityCard getDivinityCard(String cardName){
        DivinityCard div = null;
        for(DivinityCard card : listOfCards){
            if(card.getCardName().equals(cardName))
                div = card;
        }
        return div;
    }

}
