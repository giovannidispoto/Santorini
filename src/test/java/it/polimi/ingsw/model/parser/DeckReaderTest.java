package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.Type;
import org.junit.jupiter.api.Test;

import java.io.*;

import static it.polimi.ingsw.TestsStaticResources.absPathDivinitiesCardsDeck;
import static org.junit.jupiter.api.Assertions.*;

class DeckReaderTest {
    String effectAPOLLO ="Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated";

    @Test
    void readerTest() throws IOException {

        DeckReader deckReader = new DeckReader();
        //load from *.json
        Deck deck = deckReader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));

        assertNotNull(deck.getDivinityCard("APOLLO"));
        assertSame(deck.getDivinityCard("APOLLO").getCardType(), Type.MOVEMENT);
        assertSame(deck.getDivinityCard("APOLLO").getNumberOfPlayersAllowed(), 3);
        assertEquals(deck.getDivinityCard("APOLLO").getCardEffect(), effectAPOLLO);
        assertNotNull(deck.getDivinityCard("DEMETER"));
        assertSame(deck.getDivinityCard("DEMETER").getCardType(), Type.BUILD);
        assertSame(deck.getDivinityCard("DEMETER").getNumberOfPlayersAllowed(), 3);
        assertNotNull(deck.getDivinityCard("CHRONUS"));
        assertSame(deck.getDivinityCard("CHRONUS").getCardType(), Type.WIN);
        assertSame(deck.getDivinityCard("CHRONUS").getNumberOfPlayersAllowed(), 2);
    }
}