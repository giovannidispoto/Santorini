package it.polimi.ingsw.model.cards;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The Deck class model the effective deck composed by DivinityCards
 */
public class Deck {

    private List<DivinityCard> listOfCards;

    /**
     * Create new Deck from cards
     * @param listOfCards cards
     */
    public Deck(List<DivinityCard> listOfCards){
       this.listOfCards = List.copyOf(listOfCards);
    }

    /**
     * Gets divinity card from name
     * @param cardName name
     * @return divinty card
     */
    public DivinityCard getDivinityCard(String cardName){
        DivinityCard div = null;
        for(DivinityCard card : listOfCards){
            if(card.getCardName().equalsIgnoreCase(cardName))
                div = card;
        }
        return div;
    }

    /**
     * Gets cards allowed for lobby size
     * @param size size of lobby
     * @return deck
     */
    public Deck getDeckAllowed(int size){
       return new Deck(listOfCards.stream().filter(el->el.getNumberOfPlayersAllowed() >= size).collect(Collectors.toUnmodifiableList()));
    }
}
