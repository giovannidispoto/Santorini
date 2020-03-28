package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.Type;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class DeckReaderTest {

    final  StringReader reader = new StringReader("{\"listOfCards\":[\n" +
            "\t{ \"cardName\":\"Apollo\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Artemis\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Your Move: Your Worker may move one additional time, but not back to its initial space\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Athena\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Opponent’s Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Atlas\", \"cardImage\":\"URL\", \"cardType\":\"BUILD\", \"cardEffect\":\"Your Build: Your Worker may build a dome at any level\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Demeter\", \"cardImage\":\"URL\", \"cardType\":\"BUILD\", \"cardEffect\":\"Your Build: Your Worker may build one additional time, but not on the same space\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Hephaestus\", \"cardImage\":\"URL\", \"cardType\":\"BUILD\", \"cardEffect\":\"Your Build: Your Worker may build one additional block (not dome) on top of your first block\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Minotaur\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Your Move: Your Worker may move into an opponent Worker’s space, if their Worker can be forced one space straight backwards to an unoccupied space at any level\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Pan\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Win Condition: You also win if your Worker moves down two or more levels\", \"numberOfPlayersAllowed\":3},\n" +
            "\t{ \"cardName\":\"Prometheus\", \"cardImage\":\"URL\", \"cardType\":\"MOVEMENT\", \"cardEffect\":\"Your Turn: If your Worker does not move up, it may build both before and after moving\", \"numberOfPlayersAllowed\":3}\n" +
            "]}");
    @Test
    void readerTest() throws IOException {

        DeckReader deckReader = new DeckReader();
        Deck deck = deckReader.loadDeck(reader);

        assertNotNull(deck.getDivinityCard("Apollo"));
        assertTrue(deck.getDivinityCard("Apollo").getCardType() == Type.MOVEMENT);
        assertNotNull(deck.getDivinityCard("Demeter"));
        assertTrue(deck.getDivinityCard("Demeter").getCardType() == Type.BUILD);
    }
}