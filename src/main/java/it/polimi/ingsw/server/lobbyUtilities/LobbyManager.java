package it.polimi.ingsw.server.lobbyUtilities;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

import java.util.*;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

public class LobbyManager {
    private final Map<Integer, Lobby> existingLobbiesMap;
    private final Map<String, ClientHandler> playersNickNameMap;
    private final Map<ClientHandler, String> playersHandlerMap;
    private final Map<UUID, Lobby> lobbyLiveMap;

    public LobbyManager() {
        this.existingLobbiesMap = new HashMap<>();
        this.playersNickNameMap = new HashMap<>();
        this.playersHandlerMap = new HashMap<>();
        this.lobbyLiveMap = new HashMap<>();
        //Initialize Server Possibles Lobbies (null = default)
        this.existingLobbiesMap.put(2,null);
        this.existingLobbiesMap.put(3,null);
    }

    public boolean addPlayer(int lobbySize, String nickName, ClientHandler playerHandler){
        //Check if the nickname has already been registered
        if(null == playersNickNameMap.get(nickName) && isValidLobbySize(lobbySize)){
            this.playersNickNameMap.put(nickName, playerHandler);
            this.playersHandlerMap.put(playerHandler, nickName);
            this.putPlayerInLobby(lobbySize, nickName, playerHandler);
            return true;
        }
        else {
            return false;
        }
    }

    private void putPlayerInLobby(int lobbySize, String nickName, ClientHandler playerHandler) {
        if (null == this.existingLobbiesMap.get(lobbySize)) {
            //Re-set new Lobby
            this.existingLobbiesMap.put(lobbySize, new Lobby(new Controller(), lobbySize));
            this.lobbyLiveMap.put(this.existingLobbiesMap.get(lobbySize).getLobbyID(), this.existingLobbiesMap.get(lobbySize));
        }

        if (this.existingLobbiesMap.get(lobbySize).setPlayerInLobby(nickName, playerHandler)) {
            //Start Lobby
            this.lobbyLiveMap.get(this.existingLobbiesMap.get(lobbySize).getLobbyID()).getController().lobbyIsReady(
                    lobbySize,
                    nickName,
                    this.existingLobbiesMap.get(lobbySize).getPlayersNickName(),
                    this.existingLobbiesMap.get(lobbySize).getPlayersHandlerInLobby());

            System.out.println(ansiBLUE+"Lobby-Started_ID: "+this.existingLobbiesMap.get(lobbySize).getLobbyID()+ansiRESET);
            System.out.println(ansiBLUE+"Players: "+this.existingLobbiesMap.get(lobbySize).getPlayersNickName()+ansiRESET);
            //Set to default
            this.existingLobbiesMap.put(lobbySize, null);
        }
    }

    public void removePlayer(ClientHandler playerHandler){
        //Check if the nickname has already been removed
        if(null != playersHandlerMap.get(playerHandler)){
            String nickName = this.playersHandlerMap.get(playerHandler);
            this.removePlayerInLobby(nickName);
            this.playersNickNameMap.remove(nickName);
            this.playersHandlerMap.remove(playerHandler);
        }
    }

    private void removePlayerInLobby(String nickName){
        this.getLobbyThreadByLobbyID(this.playersNickNameMap.get(nickName).getLobbyID()).removePlayerInLobby(nickName);
    }

    public void deleteLobby(UUID lobbyID){
        this.lobbyLiveMap.get(lobbyID).getPlayersNickName().forEach(this.playersNickNameMap::remove);
        this.lobbyLiveMap.get(lobbyID).getPlayersHandlerInLobby().forEach(this.playersHandlerMap::remove);
        this.lobbyLiveMap.get(lobbyID).deleteAll();
        this.lobbyLiveMap.remove(lobbyID);
    }


    public boolean isValidLobbySize(int lobbySize){
        return existingLobbiesMap.containsKey(lobbySize);
    }

    public Controller getControllerByLobbyID(UUID lobbyID) {
        return lobbyLiveMap.get(lobbyID).getController();
    }

    public Lobby getLobbyThreadByLobbyID(UUID lobbyID) {
        return lobbyLiveMap.get(lobbyID);
    }

    public String getPlayerNickName(ClientHandler playerHandler){
        return this.playersHandlerMap.get(playerHandler);
    }
}
