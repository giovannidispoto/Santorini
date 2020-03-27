package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.Type;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckReaderTest {
    final  StringReader reader = new StringReader("{ \"cardName\":\"Apollo\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Artemis\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Your Move: Your Worker may move one additional time, but not back to its initial space\", \"numberOfPlayersAllowed\":3},\n");
    @Test
    void readerTest() throws IOException {

        DeckReader deckReader = new DeckReader();
        Deck deck = deckReader.loadDeck(reader);

        assertNotNull(deck.getDivinityCard("Apollo"));
        assertTrue(deck.getDivinityCard("Apollo").getCardType() == Type.MOVEMENT);
    }
}