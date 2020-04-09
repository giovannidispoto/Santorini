package it.polimi.ingsw.model.cards;

import java.util.List;

/**
 * The Deck class model the effective deck composed by DivinityCards
 */
public class Deck {

    private List<DivinityCard> listOfCards;

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
