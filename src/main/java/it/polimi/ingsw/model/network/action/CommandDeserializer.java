package it.polimi.ingsw.model.network.action;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.cards.effects.basic.BasicTurn;
import it.polimi.ingsw.model.cards.effects.basic.BlockUnder;
import it.polimi.ingsw.model.cards.effects.basic.CantLevelUp;
import it.polimi.ingsw.model.cards.effects.build.DomeEverywhere;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockAbove;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockPerimetral;
import it.polimi.ingsw.model.cards.effects.move.*;
import it.polimi.ingsw.model.cards.effects.remove.RemoveBlock;
import it.polimi.ingsw.model.cards.effects.win.JumpEffect;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Deserializer for Command
 */
public class CommandDeserializer implements JsonDeserializer<Command> {
    /**
     * Method used by Gson to deserialize correctly JSON file
     * @param jsonElement json to deserialize
     * @param type type of object
     * @param jsonDeserializationContext Context for deserialization that is passed to a custom deserializer during invocation of its method
     * @return Command executed from remote client
     * @throws JsonParseException Exception type for parsing problems, used when non-well-formed content is encountered
     */
    @Override
    public Command deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Command c = null;
        Gson gson = new Gson();

        switch(jsonElement.getAsJsonObject().get("action").getAsString()){
            case "addNewPlayer":
                PlayerInterface player = gson.fromJson(jsonElement.getAsJsonObject().get("data"), PlayerInterface.class);
                c = new AddPlayerCommand(player);
                break;
        }

        return c;
    }
}
