package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.Type;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckReaderTest {

    @Test
    void readerTest() throws IOException {

        DeckReader deckReader = new DeckReader();
        //load from *.json
        Deck deck = deckReader.loadDeck(new FileReader("src/Divinities.json"));

        assertNotNull(deck.getDivinityCard("Apollo"));
        assertSame(deck.getDivinityCard("Apollo").getCardType(), Type.MOVEMENT);
        assertNotNull(deck.getDivinityCard("Demeter"));
        assertSame(deck.getDivinityCard("Demeter").getCardType(), Type.BUILD);
    }
}