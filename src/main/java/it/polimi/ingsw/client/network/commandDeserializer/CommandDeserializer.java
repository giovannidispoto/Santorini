package it.polimi.ingsw.client.network.commandDeserializer;

import com.google.gson.*;
import it.polimi.ingsw.client.network.commands.Command;
import it.polimi.ingsw.client.network.commands.allPhases.NotExistCommand;

import java.lang.reflect.Type;

/**
 * Deserializer for Commands|Response from the server
 */
public class CommandDeserializer implements JsonDeserializer<Command> {

    /**
     * Method used by Gson to deserialize correctly JSON file
     * @param jsonElement json to deserialize
     * @param type type of object
     * @param jsonDeserializationContext Context for deserialization that is passed to a custom deserializer during invocation of its method
     * @return Command executed from Server
     * @throws JsonParseException Exception type for parsing problems, used when non-well-formed content is encountered
     */
    @Override
    public Command deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Command c;

        String action = jsonElement.getAsJsonObject().get("action").getAsString();
        //Load DeserializationMap
        DeserializerHashMap deserializerHashMap = DeserializerHashMap.getDeserializerHashMapInstance();
        //Pick correct Deserialization  & Return Specific Command
        if(null == deserializerHashMap.getMapCommand(action))
        {
            //If the message is not recognized by the client but consists of "action:", the error is written on the logger
            c = new NotExistCommand(action);
        }else {
            c = deserializerHashMap.getMapCommand(action).command(jsonElement);
        }
        
        return c;
    }
}
