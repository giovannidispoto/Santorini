package it.polimi.ingsw.server.network.commandDeserializer;

import com.google.gson.*;
import it.polimi.ingsw.server.network.commands.Command;
import it.polimi.ingsw.server.network.commands.NotExistCommand;

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
        Command c;

        String action = jsonElement.getAsJsonObject().get("action").getAsString();
        //Load DeserializationMap

        //Pick correct Deserialization  & Return Specific Command
        if(CommandMap.getCommandMapInstance().getMapCommand(action) != null)
                 c = CommandMap.getCommandMapInstance().getMapCommand(action).command(jsonElement);
        else{
            c = new NotExistCommand("");
        }


        return c;

       /* Command c = null;
        Gson gson = new Gson();

        switch(jsonElement.getAsJsonObject().get("action").getAsString()){
            case "addPlayer":
                c = new AddPlayerCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbySize").getAsInt());
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
                c = new SelectWorkerCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("x").getAsInt(),jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("y").getAsInt());
                break;
            case "playStep":
                c = new PlayStepCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("x").getAsInt(),jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("y").getAsInt());
                break;
            case "startTurn":
                c = new StartTurnCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("basicTurn").getAsBoolean(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString());
                break;
            case "skipStep":
                c = new SkipStepCommand();
                break;
            case "setPlayerCard":
                c = new SetPlayerCardCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(), jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("card").getAsString());
                break;
            case "getDeck":
                c = new GetDeckCommand();
                break;
            case "setPickedCards":
                c = new SetPickedCardsCommand(new Gson().fromJson(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("cards"), new TypeToken<ArrayList<String>>(){}.getType()));
                break;
            case "getCardsInGame":
                c = new GetCardsInGameCommand();
                break;
        }

        return c;*/
    }
}
