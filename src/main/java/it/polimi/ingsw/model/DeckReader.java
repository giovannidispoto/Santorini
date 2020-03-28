package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.Deck;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * The DeckReader class read all the card from JSON and create Deck
 */
public class DeckReader {

    /**
     * Load deck from reader passed
     * @param reader file to read
     * @return deck of divinity card
     * @throws IOException
     */
    public Deck loadDeck(Reader reader) throws IOException {
        BufferedReader buff = new BufferedReader(reader);
        Gson gson = new Gson();

        return gson.fromJson(buff, Deck.class);
    }

}
