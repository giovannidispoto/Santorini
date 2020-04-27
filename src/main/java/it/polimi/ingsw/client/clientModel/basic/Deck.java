package it.polimi.ingsw.client.clientModel.basic;

import java.util.List;

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

    //TODO: improve pickCards() in CLIBuilder
    /** Remove the matching card from the deck by its name
     *  N:B: No case sensitive
     * @param cardName  Card Name (English)
     */
    public void removeDivinityCard(String cardName){
        listOfCards.removeIf(card -> card.getCardName().equalsIgnoreCase(cardName));
    }

    public List<DivinityCard> getAllCards(){return listOfCards;}

}
