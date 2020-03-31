package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockAbove;
import it.polimi.ingsw.model.cards.effects.move.BlockUnder;
import it.polimi.ingsw.model.cards.effects.move.ExtraMove;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DivinityEffectReaderTest {

    final StringReader reader = new StringReader("{\n" +
            "\t\"cardsEffects\": [{\n" +
            "\t\t\t\"cardName\": \"Zeus\",\n" +
            "\t\t\t\"Effect\": {\n" +
            "\t\t\t\t\"effectName\": \"BlockUnder\"\n" +
            "\t\t\t}\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"cardName\": \"Artemis\",\n" +
            "\t\t\t\"Effect\": {\n" +
            "\t\t\t\t\"effectName\": \"ExtraMove\",\n" +
            "\t\t\t\t\"numberOfExtraMoves\": 1\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t]\n" +
            "}");

    @Test
    void readDivinity() {
        DivinityEffectReader der = new DivinityEffectReader();
        Map<String, Turn> map = der.load(reader);

        assertTrue(map.containsKey("Zeus"));
        assertTrue(map.get("Zeus") instanceof BlockUnder);
        assertTrue(map.containsKey("Artemis"));
        assertTrue(map.get("Artemis") instanceof ExtraMove);

    }
}
