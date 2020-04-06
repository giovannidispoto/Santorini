package it.polimi.ingsw.model.parser;

import com.google.gson.*;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.effects.basic.BasicTurn;
import it.polimi.ingsw.model.cards.effects.basic.BlockUnder;
import it.polimi.ingsw.model.cards.effects.build.DomeEverywhere;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockAbove;
import it.polimi.ingsw.model.cards.effects.move.ExtraMove;
import it.polimi.ingsw.model.cards.effects.move.NoMoveUp;
import it.polimi.ingsw.model.cards.effects.move.PushCharacter;
import it.polimi.ingsw.model.cards.effects.move.SwitchCharacter;
import it.polimi.ingsw.model.cards.effects.win.JumpEffect;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserializer for DivinityEffectReader
 */
public class DivinityEffectDeserializer implements JsonDeserializer<Map<String,Turn>> {
    /**
     * Method used by Gson to deserialize correctly JSON file
     * @param jsonElement json to deserialize
     * @param type type of object
     * @param jsonDeserializationContext
     * @return association between card name and effect
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
                case "SwitchCharacter":
                    t = new SwitchCharacter();
                    break;
                case "NoMoveUp":
                    t = new NoMoveUp();
                    break;
                case "DomeEverywhere":
                    t = new DomeEverywhere();
                    break;
                case "ExtraBlockAbove":
                    t = new ExtraBlockAbove(jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("buildInSameCell").getAsBoolean(),jsonE.getAsJsonObject().get("Effect").getAsJsonObject().get("numberOfExtraBlocks").getAsInt());
                    break;
                case "PushCharacter":
                    t = new PushCharacter();
                    break;
                case "JumpEffect":
                    t = new JumpEffect();
                    break;
                case "BasicTurn":
                    t = new BasicTurn();
                    break;
            }
            map.put(cardName,t);
        }

        return map;
    }
}
