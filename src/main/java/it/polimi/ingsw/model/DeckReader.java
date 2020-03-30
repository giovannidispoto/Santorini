package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.Deck;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reads all the cards from a JSON file and it's used to create a Deck object
 */
public class DeckReader {

    /**
     * Loads the card deck from a file
     * @param reader file that has to be read
     * @return Deck object (of divinity cards)
     * @throws IOException exception
     */
    public Deck loadDeck(Reader reader) throws IOException {
        BufferedReader buff = new BufferedReader(reader);
        Gson gson = new Gson();

        return gson.fromJson(buff, Deck.class);
    }

}
