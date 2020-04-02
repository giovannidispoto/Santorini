package it.polimi.ingsw.model.parser;

import com.google.gson.*;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.effects.basic.BlockUnder;
import it.polimi.ingsw.model.cards.effects.move.ExtraMove;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializer for DivinityEffectReader
 */
public class DivinityEffectDeserializer implements JsonDeserializer<Map<String,Turn>> {
    /**
     *
     * @param jsonElement
     * @param type
     * @param jsonDeserializationContext
     * @return
     * @throws JsonParseException
     */
    @Override
    public Map<String, Turn> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Map<String, Turn> map = new HashMap<>();
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("cardsEffects").getAsJsonArray();
        Turn t = null;
        for(JsonElement jsonE : jsonArray){
            String cardName = jsonE.getAsJsonObject().get("cardName").getAsString();
            switch(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("effectName").getAsString()){
                case "ExtraMove":
                    t = new ExtraMove(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("numberOfExtraMoves").getAsInt());
                    break;
                case "BlockUnder":
                    t = new BlockUnder();
                    break;
            }
            map.put(cardName,t);
        }

        return map;
    }
}
