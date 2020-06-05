package it.polimi.ingsw.server.lobbyUtilities;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.network.ClientHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents each individual lobby
 */
public class Lobby {
    private Controller controller;
    private ExecutorService executorService;
    private Map<String, ClientHandler> playersHandlerInLobby;
    private final int maxPlayers;
    private int currentPlayers;
    private final UUID lobbyID;

    /**
     * Lobby builder, enter the controller, tie a unique id to the lobby and assign the maximum lobby size
     * @param controller    of the lobby
     * @param maxPlayers    of the lobby
     */
    public Lobby(Controller controller, int maxPlayers) {
        this.controller = controller;
        this.executorService = Executors.newFixedThreadPool(1);
        this.playersHandlerInLobby = new HashMap<>();
        this.maxPlayers = maxPlayers;
        this.currentPlayers = 0;
        this.lobbyID = UUID.randomUUID();
    }

    //----------    LOBBY MANAGEMENT

    /**
     * Add the player to the lobby
     * @param nickName  unique
     * @param playerHandler of the player with this nickName
     * @return  true if the lobby is full, false if there is still room
     */
    public boolean setPlayerInLobby(String nickName, ClientHandler playerHandler) {
        this.playersHandlerInLobby.put(nickName, playerHandler);
        playerHandler.setLobbyID(this.lobbyID);
        this.currentPlayers ++;
        return (this.currentPlayers == this.maxPlayers);
    }

    /**
     * Removes a player from the lobby, should only be used while the lobby is still waiting, free up space for another player.
     * @param nickName unique
     */
    public void removePlayerInLobby(String nickName) {
        this.playersHandlerInLobby.remove(nickName);
        this.currentPlayers --;
    }

    /**
     * Delete all the contents of the Lobby object and shutdown executorService
     */
    public void deleteAll(){
        controller = null;
        executorService.shutdown();
        executorService = null;
        playersHandlerInLobby = null;
    }

    //----------    GETTERS & SETTERS


    /**
     * Get the lobby ID
     * @return  UUID : lobby ID
     */
    public UUID getLobbyID() {
        return lobbyID;
    }

    /**
     * Get the map that links every nickName to it's handler
     * @return Map(String, ClientHandler)
     */
    public Map<String, ClientHandler> getPlayersHandlerInLobby() {
        return this.playersHandlerInLobby;
    }

    /**
     * Get a list containing all the nicknames of the players in the lobby
     * @return  List of nickNames
     */
    public List<String> getPlayersNickName(){
        List<String> playersNickname = new ArrayList<>();
        this.playersHandlerInLobby.forEach((nickName, handler) -> playersNickname.add(nickName));
        return playersNickname;
    }

    /**
     * Get the lobby Controller
     * @return Controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Get the lobby ExecutorService
     * @return ExecutorService
     */
    public ExecutorService getLobbyExecutorService() {
        return executorService;
    }

}
