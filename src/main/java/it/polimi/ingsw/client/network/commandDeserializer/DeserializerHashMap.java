package it.polimi.ingsw.client.network.commandDeserializer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.client.network.commands.allPhases.BattlefieldCommands;
import it.polimi.ingsw.client.network.commands.allPhases.PingCommand;
import it.polimi.ingsw.client.network.commands.finishPhase.LoseCommand;
import it.polimi.ingsw.client.network.commands.finishPhase.ServerErrorCommand;
import it.polimi.ingsw.client.network.commands.finishPhase.WinCommand;
import it.polimi.ingsw.client.network.commands.lobbyPhase.*;
import it.polimi.ingsw.client.network.commands.matchPhase.*;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.CellInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.CellMatrixInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.PlayerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that contains all possible network messages that can be deserializes and therefore treated by the command pattern
 */
public class  DeserializerHashMap  {
    private Map<String, ProcessingCommand> commandMap;
    private static DeserializerHashMap instance = null;

    /**
     * Factory method that returns the DeserializerHashMap instance (Singleton)
     * @return DeserializerHashMap object
     */
    public static DeserializerHashMap getDeserializerHashMapInstance(){
        if(null == instance){
            instance = new DeserializerHashMap();
            instance.initializeDeserializerHashMap();
        }
        return instance;
    }

    /** Get Specific Command from the commandMap
     *
     * @param action    key of the command requested
     * @return  ProcessingCommand object
     */
    public ProcessingCommand getMapCommand(String action){
        return this.commandMap.get(action);
    }

    /**
     *  Initialize the map with the commands present in the loading phase
     */
    private void initializeDeserializerHashMap() {
        this.commandMap = new HashMap<>();
        //Load Specific Deserialization Methods in commandMap
        loadAddPlayerResponse();
        loadSetPickedCards();
        loadGetDeckResponse();
        loadSetPlayerCard();
        loadSetWorkersPosition();
        loadGetPlayersResponse();
        loadGetBattlefieldResponse();
        loadBattlefieldUpdate();

        loadActualPlayer();
        loadSetStartTurnResponse();
        loadWorkerViewUpdate();
        loadPlayStepResponse();
        loadSkipStepResponse();

        loadYouLose();
        loadYouWin();
        loadServerError();
        loadPing();
    }

    //Specific Deserialization Methods

    //1
    private void loadAddPlayerResponse(){
        this.commandMap.put("addPlayerResponse", jsonElement -> new AddPlayerCommand(
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("validNick").getAsBoolean(),
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbyState").getAsBoolean(),
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbySize").getAsInt(),
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("fullLobby").getAsBoolean()));
    }

    //2
    private void loadSetPickedCards(){
        this.commandMap.put("setPickedCards", jsonElement -> new SetPickedCardsCommand(
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString()));
    }

    //3
    private void loadGetDeckResponse(){
        this.commandMap.put("getDeckResponse", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), GetDeckCommand.class));
    }

    //4
    private void loadSetPlayerCard(){
        this.commandMap.put("setPlayerCard", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SetPlayerCardCommand.class));
    }

    //5
    private void loadSetWorkersPosition(){
        this.commandMap.put("setWorkersPosition", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SetWorkersPositionCommand.class));
    }

    //6
    private void loadGetPlayersResponse(){
        this.commandMap.put("getPlayersResponse", jsonElement -> new GetPlayersCommand(deserializePlayers(jsonElement)));
    }

    //7
    private void loadGetBattlefieldResponse(){
        this.commandMap.put("getBattlefieldResponse", jsonElement -> new BattlefieldCommands(deserializeCellMatrix(jsonElement), "getBattlefieldResponse"));
    }

    //8
    private void loadBattlefieldUpdate(){
        this.commandMap.put("battlefieldUpdate", jsonElement -> new BattlefieldCommands(deserializeCellMatrix(jsonElement), "battlefieldUpdate"));
    }

    //9
    private void loadActualPlayer(){
        this.commandMap.put("actualPlayer", jsonElement -> new ActualPlayerCommand(
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString()));
    }

    //10
    private void loadSetStartTurnResponse(){
        this.commandMap.put("setStartTurnResponse", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SetStartTurnResponse.class));
    }

    //11
    private void loadWorkerViewUpdate(){
        this.commandMap.put("workerViewUpdate", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), WorkerViewUpdateCommand.class));
    }

    //12
    private void loadPlayStepResponse(){
        this.commandMap.put("playStepResponse", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), PlayStepResponse.class));
    }

    //13
    private void loadSkipStepResponse(){
        this.commandMap.put("skipStepResponse", jsonElement -> new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), SkipStepResponse.class));
    }

    //14
    private void loadYouWin(){
        this.commandMap.put("youWin", jsonElement -> new WinCommand());
    }

    //15
    private void loadYouLose(){
        this.commandMap.put("youLose", jsonElement -> new LoseCommand());
    }

    //16
    private void loadServerError(){
        this.commandMap.put("serverError", jsonElement -> new ServerErrorCommand(
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("error").getAsString()));
    }

    //17
    private void loadPing(){
        this.commandMap.put("ping", jsonElement -> new PingCommand());
    }

    //Common Methods

    private CellInterface[][] deserializeCellMatrix (JsonElement jsonElement){
        CellMatrixInterface cellMatrix = new Gson().fromJson(jsonElement.getAsJsonObject().get("data"), CellMatrixInterface.class);
        return cellMatrix.getCellMatrix();
    }

    private List<PlayerInterface> deserializePlayers (JsonElement jsonElement){
        List<PlayerInterface> players = new ArrayList<>();
        //Start Deserialization of jsonElement
        JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("players").getAsJsonArray();
        for(JsonElement jsonE : jsonArray) {
            players.add(new Gson().fromJson(jsonE, PlayerInterface.class));
        }
        return players;
    }

}
