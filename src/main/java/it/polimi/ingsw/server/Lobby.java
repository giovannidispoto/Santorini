package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Lobby {
    private Controller controller;
    private ExecutorService executorService;
    private Map<String, ClientHandler> playersHandlerInLobby;
    private final int maxPlayers;
    private int currentPlayers;
    private final UUID lobbyID;

    public Lobby(Controller controller, int maxPlayers) {
        this.controller = controller;
        this.executorService = Executors.newFixedThreadPool(1);
        this.playersHandlerInLobby = new HashMap<>();
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0;
        this.lobbyID = UUID.randomUUID();
    }

    public boolean setPlayerInLobby(String nickName, ClientHandler playerHandler) {
        this.playersHandlerInLobby.put(nickName, playerHandler);
        playerHandler.setLobbyID(this.lobbyID);
        this.currentPlayers ++;
        return (this.currentPlayers == this.maxPlayers);
    }

    public void removePlayerInLobby(String nickName) {
        this.playersHandlerInLobby.remove(nickName);
        this.currentPlayers --;
    }

    public UUID getLobbyID() {
        return lobbyID;
    }

    public Map<String, ClientHandler> getPlayersHandlerInLobby() {
        return this.playersHandlerInLobby;
    }

    public List<String> getPlayersNickName(){
        List<String> playersNickname = new ArrayList<>();
        this.playersHandlerInLobby.forEach((nickName, handler) -> playersNickname.add(nickName));
        return playersNickname;
    }

    public Controller getController() {
        return controller;
    }

    public ExecutorService getLobbyExecutorService() {
        return executorService;
    }

    public void deleteAll(){
        controller = null;
        executorService.shutdown();
        executorService = null;
        playersHandlerInLobby = null;
    }
}
