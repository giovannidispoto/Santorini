package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        String line;
        Gson gson = new Gson();
        List<DivinityCard> cards = new ArrayList<>();

        while((line = buff.readLine()) != null) {
            line = (line.charAt(line.length()-1) == ',') ? line.substring(0,line.length()-1): line;
            DivinityCard card = gson.fromJson(line, DivinityCard.class);
            cards.add(card);
        }

        return new Deck(cards);
    }

}
