package it.polimi.ingsw.client.clientModel.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * The Deck class model the effective deck composed by DivinityCards
 */
public class Deck {

    private final List<DivinityCard> listOfCards;

    /**
     * Create new Deck from cards
     * @param listOfCards cards
     */
    public Deck(List<DivinityCard> listOfCards){
       this.listOfCards = listOfCards;
    }

    /**
     * Gets divinity card from name
     * N.B: No case sensitive
     * @param cardName Card Name (English)
     * @return divinity card
     */
    public DivinityCard getDivinityCard(String cardName){
        DivinityCard div = null;
        for(DivinityCard card : listOfCards){
            if(card.getCardName().equalsIgnoreCase(cardName))
                div = card;
        }
        return div;
    }

    /** Function that returns all the names of the cards in the deck
     *
     * @return  list containing cards names
     */
    public List<String> getCardsNames(){
        List<String> cardsNames = new ArrayList<>();
        for(DivinityCard card : listOfCards){
            cardsNames.add(card.getCardName());
        }
        return cardsNames;
    }

    /**
     * Cards Getter
     * @return  All the cards in the deck
     */
    public List<DivinityCard> getAllCards(){
        return listOfCards;
    }
}
