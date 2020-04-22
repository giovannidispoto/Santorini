package it.polimi.ingsw.client.network.actions;

import com.google.gson.*;
import it.polimi.ingsw.client.network.actions.data.CellInterface;
import it.polimi.ingsw.client.network.actions.data.PlayerInterface;

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
            case "battlefieldUpdate":
                c = new BattlefieldUpdateCommand(deserializeBoards(jsonElement));
                break;
            case "workerViewUpdate":
                c = new WorkerViewUpdateCommand(deserializeBoards(jsonElement));
                break;
            case "getPlayers":
                c = new GetPlayersCommand(deserializePlayers(jsonElement));
                break;
            case "getWorkersID":
                //TODO new command
                break;
        }

        return c;
    }

    private CellInterface[][] deserializeBoards (JsonElement jsonElement){
        //Needed for boards deserialization
        int boardRow = 5;
        int boardCol = 5;
        CellInterface[][] board = new CellInterface[boardRow][boardCol];
        int i = 0, j;
        //Start Deserialization of jsonElement
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonArray();
        for(JsonElement jsonE : jsonArray) {
            JsonArray jsonArray2 = jsonE.getAsJsonObject().getAsJsonArray();
            j=0;
            for(JsonElement jsonEl : jsonArray2) {
                board[i][j] = new Gson().fromJson(jsonEl, CellInterface.class);
                j++;
            }
            i++;
        }
        return board;
    }

    private List<PlayerInterface> deserializePlayers (JsonElement jsonElement){
        List<PlayerInterface> players = new ArrayList<>();
        //TODO Deserialization of players from data
        return players;
    }
}
