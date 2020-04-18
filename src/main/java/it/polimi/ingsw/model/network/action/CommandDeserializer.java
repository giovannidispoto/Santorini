package it.polimi.ingsw.model.network.action;

import com.google.gson.*;
import it.polimi.ingsw.model.network.action.data.PlayerInterface;

import java.lang.reflect.Type;

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
            case "setPlayerReady":
                c = new SetReadyPlayerCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString());
                break;
            case "getWorkersID":
                c = new GetWorkersIDCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString());
                break;
            case "getPlayers":
                c = new GetPlayersCommand();
                break;
            case "setInitialWorkerPosition":
                c = new SetInitialWorkerPositionCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("worker").getAsInt(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("x").getAsInt(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("y").getAsInt());
                break;
            case "selectWorker":
                c = new SelectWorkerCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("worker").getAsInt());
                break;
            case "playStep":
                c = new PlayStepCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("x").getAsInt(),jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("y").getAsInt());
                break;
            case "skipStep":
                c = new SkipStepCommand();
                break;

        }

        return c;
    }
}
