package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.server.actions.commands.*;
import it.polimi.ingsw.server.actions.data.WorkerPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandMap {
    private Map<String, ProcessingCommand> commandMap;
    private static CommandMap instance = null;

    /**
     * Factory method that returns the DeserializerHashMap instance (Singleton)
     * @return DeserializerHashMap object
     */
    public static CommandMap getCommandMapInstance(){
        if(null == instance){
            instance = new CommandMap();
            instance.initializeCommandMap();
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
    private void initializeCommandMap() {
        this.commandMap = new HashMap<>();
        //Load Specific Deserialization Methods in commandMap
        loadAddPlayerResponse();
        loadSetPickedCards();
        loadGetDeck();
        loadSetPlayerCard();
        loadGetWorkersID();
        loadGetPlayers();
        loadSelectWorker();
        loadPlayStep();
        loadStartTurn();
        loadSkipStep();
        loadGetCardsInGame();
        loadSetWorkersPosition();
        loadGetBattlefield();
    }



    //Specific Deserialization Methods

    //1
    private void loadAddPlayerResponse(){
        this.commandMap.put("addPlayer", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new AddPlayerCommand(
                jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("lobbySize").getAsInt());
            }
        });
    }

    //2
    private void loadSetPickedCards(){
        this.commandMap.put("setPickedCards", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new SetPickedCardsCommand(
                        new Gson().fromJson(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("cards"), new TypeToken<ArrayList<String>>(){}.getType()));
            }
        });
    }

    //3
    private void loadGetDeck(){
        this.commandMap.put("getDeck", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return  new GetDeckCommand();
            }
        });
    }

    //4
    private void loadSetPlayerCard(){
        this.commandMap.put("setPlayerCard", new ProcessingCommand() {
            public Command command(JsonElement jsonElement) {
                return new SetPlayerCardCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("card").getAsString()
                );
            }
        });
    }
    //5
    private void loadGetWorkersID(){
        this.commandMap.put("getWorkersID", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
               return new GetWorkersIDCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString()
                );
            }
        });
    }
    //6
    private void loadGetPlayers(){
        this.commandMap.put("getPlayers", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new GetPlayersCommand();
            }
        });
    }
    //7
    private void loadSetWorkersPosition(){
        this.commandMap.put("setWorkersPosition", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
               return new SetWorkersPosition(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(),
                        new Gson().fromJson(jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("workersPosition"),new TypeToken<ArrayList<WorkerPosition>>(){}.getType())
                );
            }
        });
    }
    //8
    private void loadSelectWorker(){
        this.commandMap.put("selectWorker", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new SelectWorkerCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("x").getAsInt(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("y").getAsInt()
                );
            }
        });
    }
    //9
    private void loadPlayStep(){
        this.commandMap.put("playStep", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new PlayStepCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("x").getAsInt(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("y").getAsInt()
                );
            }
        });
    }
    //10
    private void loadStartTurn(){
        this.commandMap.put("setStartTurn", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new SetStartTurnCommand(
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("playerNickname").getAsString(),
                        jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("basicTurn").getAsBoolean()
                );
            }
        });
    }
    //11
    private void loadSkipStep(){
        this.commandMap.put("skipStep", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new SkipStepCommand();
            }
        });
    }
    //12
    private void loadGetCardsInGame(){
        this.commandMap.put("getCardsInGame", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new GetCardsInGameCommand();
            }
        });
    }

    //13
    private void loadGetBattlefield() {
        this.commandMap.put("getBattlefield", new ProcessingCommand() {
            @Override
            public Command command(JsonElement jsonElement) {
                return new GetBattlefieldCommand();
            }
        });
    }

}
