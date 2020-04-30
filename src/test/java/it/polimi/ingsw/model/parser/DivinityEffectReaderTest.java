package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.effects.basic.BlockUnder;
import it.polimi.ingsw.model.cards.effects.move.ExtraMove;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DivinityEffectReaderTest {

    final StringReader reader = new StringReader("{\n" +
            "\t\"cardsEffects\": [{\n" +
            "\t\t\t\"cardName\": \"ZEUS\",\n" +
            "\t\t\t\"Effect\": {\n" +
            "\t\t\t\t\"effectName\": \"BlockUnder\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"cardName\": \"ARTEMIS\",\n" +
            "\t\t\t\"Effect\": {\n" +
            "\t\t\t\t\"effectName\": \"ExtraMove\",\n" +
            "\t\t\t\t\"numberOfExtraMoves\": 1\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}");

    @Test
    void readDivinity() throws IOException {
        DivinityEffectReader der = new DivinityEffectReader();
        Map<String, Turn> map = der.load(new FileReader("src/CardsEffect.json"));

        assertTrue(map.containsKey("ZEUS"));
        assertTrue(map.get("ZEUS") instanceof BlockUnder);
        assertTrue(map.containsKey("ARTEMIS"));
        assertTrue(map.get("ARTEMIS") instanceof ExtraMove);

    }
}
