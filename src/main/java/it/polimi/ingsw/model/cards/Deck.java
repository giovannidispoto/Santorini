package it.polimi.ingsw.model.cards;

import java.util.List;

/**
 * The Deck class model the effective deck composed by DivinityCards
 */
public class Deck {

    private List<DivinityCard> listOfCards;
    private Deck deckInstance = null;

    private Deck(){
        //Inizizialize deck with cards
    }

    private Deck get(){
        if(deckInstance == null)
            deckInstance = new Deck();
        return deckInstance;
    }



}
