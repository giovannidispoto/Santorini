package it.polimi.ingsw.client.network.actions;

import com.google.gson.*;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializer for Commands from the server
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
        Command c = null;

        switch(jsonElement.getAsJsonObject().get("action").getAsString()){
            case "addPlayer":
                c = new AddPlayerCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("validNick").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbyState").getAsBoolean(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbySize").getAsInt());
                break;
            case "setPickedCards":
                c = new SetPickedCardsCommand(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbySize").getAsInt());
                break;
            case "getDeckResponse":
                c = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), GetDeckCommand.class);
                break;
            case "battlefieldUpdate":
                c = new BattlefieldUpdateCommand(deserializeCellMatrix(jsonElement));
                break;
            case "workerViewUpdate":
                c = new WorkerViewUpdateCommand(deserializeCellMatrix(jsonElement));
                break;
            case "getPlayers":
                c = new GetPlayersCommand(deserializePlayers(jsonElement));
                break;
            case "getWorkersID":
                c = new GetWorkersIDCommand(deserializeWorkersID(jsonElement));
                break;
        }

        return c;
    }

    private CellInterface[][] deserializeCellMatrix (JsonElement jsonElement){
        CellMatrixInterface cellMatrix = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), CellMatrixInterface.class);
        return cellMatrix.getCellMatrix();
    }

    private List<PlayerInterface> deserializePlayers (JsonElement jsonElement){
        List<PlayerInterface> players = new ArrayList<>();
        //Start Deserialization of jsonElement
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonArray();
        for(JsonElement jsonE : jsonArray) {
            players.add(new Gson().fromJson(jsonE, PlayerInterface.class));
        }
        return players;
    }

    private List<Integer> deserializeWorkersID (JsonElement jsonElement){
        List<Integer> workersID = new ArrayList<>();
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonArray();
        for(JsonElement jsonE : jsonArray) {
            workersID.add(jsonE.getAsInt());
        }
        return workersID;
    }
}
