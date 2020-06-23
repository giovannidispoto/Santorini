package it.polimi.ingsw.server.lobbyUtilities;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.network.ClientHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

/**
 * Server class that manages the lobbies, with phases of connection disconnection, end of game, interruption of game
 */
public class LobbyManager {
    private static final String nickNameRegex = "^[a-zA-Z0-9_.-]+$";
    private final Map<Integer, Lobby> existingLobbiesMap;
    private final Map<String, ClientHandler> playersNickNameMap;
    private final Map<ClientHandler, String> playersHandlerMap;
    private final Map<UUID, Lobby> lobbyLiveMap;
    private final int maxNumLobbiesManaged;
    private int waitingLobbies = 0;

    /**
     * Lobby Manager builder, in addition to initializing it here, you can enter the sizes of the lobbies existing on the server
     */
    public LobbyManager(int maxNumLobbiesManaged) {
        this.existingLobbiesMap = new HashMap<>();
        this.playersNickNameMap = new HashMap<>();
        this.playersHandlerMap = new HashMap<>();
        this.lobbyLiveMap = new HashMap<>();
        //Initialize Server Possibles Lobbies (null = default)
        this.existingLobbiesMap.put(2,null);
        this.existingLobbiesMap.put(3,null);
        this.maxNumLobbiesManaged = maxNumLobbiesManaged;
    }

    //----------    LOBBY MANAGEMENT


    /**
     * Request function to add player to a lobby, check that the nickname is unique, if the maximum number of active lobbies has not been reached and at least nickName length 1,
     * that the size of the chosen lobby is implemented on the server (this function does not differentiate the two errors,
     * if it returns false it can only be said that the player has not been added to the lobby),
     * the handler and nickname are linked and uniquely represent a player.
     *
     * @param lobbySize size of lobby chosen
     * @param nickName  chosen, must be unique
     * @param playerHandler handler from which the request originates (therefore tied to the player)
     * @return  true, if it has been added correctly, false if it has not been added for wrong lobbySize or for nickName already present
     */
    public boolean addPlayer(int lobbySize, String nickName, ClientHandler playerHandler){
        //Input string treatment at least length == 1
        if(nickName.matches(nickNameRegex))
            nickName = nickName.toLowerCase(Locale.ROOT);
        else
            return false;
        //Check if the nickname has already been registered
        if(null == playersNickNameMap.get(nickName) && isValidLobbySize(lobbySize) && !isReachedMaxNumLobbies()){
            System.out.println(ansiGREEN + "Player_Registered: " + nickName + ansiRESET);
            this.playersNickNameMap.put(nickName, playerHandler);
            this.playersHandlerMap.put(playerHandler, nickName);
            this.putPlayerInLobby(lobbySize, nickName, playerHandler);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * (Operation done without checks)<br>
     * Inserts the player in a lobby that has not yet started, if the lobby is filled with this request,
     * the lobby is started with a request to the controller, which will take care of notifying clients, a new empty lobby is created to welcome new players
     * @param lobbySize size of lobby chosen
     * @param nickName chosen, must be unique
     * @param playerHandler handler from which the request originates (therefore tied to the player)
     */
    private void putPlayerInLobby(int lobbySize, String nickName, ClientHandler playerHandler) {
        if (null == this.existingLobbiesMap.get(lobbySize)) {
            //Re-set new Lobby
            this.existingLobbiesMap.put(lobbySize, new Lobby(new Controller(), lobbySize));
            this.lobbyLiveMap.put(this.existingLobbiesMap.get(lobbySize).getLobbyID(), this.existingLobbiesMap.get(lobbySize));
            this.waitingLobbies++;
        }

        if (this.existingLobbiesMap.get(lobbySize).setPlayerInLobby(nickName, playerHandler)) {
            //Start Lobby
            this.lobbyLiveMap.get(this.existingLobbiesMap.get(lobbySize).getLobbyID()).getController().lobbyIsReady(
                    lobbySize,
                    nickName,
                    this.existingLobbiesMap.get(lobbySize).getPlayersNickName(),
                    this.existingLobbiesMap.get(lobbySize).getPlayersHandlerInLobby(),
                    this.existingLobbiesMap.get(lobbySize).getLobbyID(),
                    this);

            System.out.println(ansiBLUE+"Lobby-Started_ID: "+this.existingLobbiesMap.get(lobbySize).getLobbyID()+ansiRESET);
            System.out.println(ansiBLUE+"Players: "+this.existingLobbiesMap.get(lobbySize).getPlayersNickName()+ansiRESET);
            //Set to default
            this.existingLobbiesMap.put(lobbySize, null);
            this.waitingLobbies--;
            System.out.println(ansiGREEN+"ACTIVE_LOBBIES: "+activeLobbies()+ansiRESET);
        }
    }

    /**
     * Removes a player who was waiting for a match to start, but was already registered in a lobby,
     * should not be called if the match has already started, <br>
     * this is because the removed player can reconnect with the same nickname and be reassigned to another lobby,
     * if the match is in progress even if the player is deleted from the match, it must not be removed from the server !!!
     * @param playerHandler handler from which the request originates (therefore tied to the player)
     */
    public void removePlayer(ClientHandler playerHandler){
        //Check if the nickname has already been removed
        if(null != playersHandlerMap.get(playerHandler)){
            String nickName = this.playersHandlerMap.get(playerHandler);
            this.removePlayerInLobby(nickName);
            this.playersNickNameMap.remove(nickName);
            this.playersHandlerMap.remove(playerHandler);
        }
    }

    /**
     * Eliminate the player from the lobby where he was waiting
     * @param nickName chosen, must be unique
     */
    private void removePlayerInLobby(String nickName){
        this.getLobbyByLobbyID(this.playersNickNameMap.get(nickName).getLobbyID()).removePlayerInLobby(nickName);
    }

    /**
     * Eliminate the lobby, a function to call in case the lobby was in the match phase, usually in the face of a sudden error,
     * it only deals with eliminating players from LobbyManager, it does not communicate with the handlers
     * @param lobbyID Lobby id to be deleted
     */
    private void deleteLobby(UUID lobbyID){
        this.lobbyLiveMap.get(lobbyID).getPlayersNickName().forEach(this.playersNickNameMap::remove);
        this.lobbyLiveMap.get(lobbyID).getPlayersHandlerInLobby().forEach((nick, clientHandler) -> this.playersHandlerMap.remove(clientHandler));
        this.lobbyLiveMap.get(lobbyID).deleteAll();
        this.lobbyLiveMap.remove(lobbyID);
        System.out.println(ansiRED + "Lobby-Deleted_ID: " + lobbyID + ansiRESET);
        System.out.println(ansiGREEN+"ACTIVE_LOBBIES: "+activeLobbies()+ansiRESET);
    }

    /**
     * Call when when the game ended correctly (can only be called once per game),
     * Its job is to notify all the handlers in the lobby and delete all the lobby & players from the server
     * @param lobbyID Lobby id on which to call the method
     */
    public void gameEnded(UUID lobbyID){
        this.lobbyLiveMap.get(lobbyID).getPlayersHandlerInLobby().forEach((nick, handler) -> handler.gameEnded());
        System.out.println(ansiGREEN + "Game-Finished_Lobby_ID: " + lobbyID + ansiRESET);
        this.deleteLobby(lobbyID);
    }

    /**
     * Call when a player first disconnects (can only be called once per game)
     * Its job is to shutdown all the handlers in the lobby and delete all the lobby & players from the server
     * @param lobbyID Lobby id to be deleted
     */
    public void clientDisconnected(UUID lobbyID){
        this.lobbyLiveMap.get(lobbyID).getPlayersHandlerInLobby().forEach((nick, handler) -> handler.disconnectionShutDown());
        this.deleteLobby(lobbyID);
    }


    //----------    GETTERS & SETTERS


    /**
     * Asks the lobbyManager if the passed size is accepted as lobbySize on the server
     * @param lobbySize lobby size choose
     * @return  true, if lobbies of that size are accepted, false if they are never accepted
     */
    public boolean isValidLobbySize(int lobbySize){
        return existingLobbiesMap.containsKey(lobbySize);
    }

    /**
     * Get a lobby controller by passing the lobby ID
     * @param lobbyID Id to look for
     * @return  Controller object linked to the lobby
     */
    public Controller getControllerByLobbyID(UUID lobbyID) {
        return lobbyLiveMap.get(lobbyID).getController();
    }

    /**
     * Get lobby object based on lobby Id
     * @param lobbyID Id to look for
     * @return Lobby object (It's the real lobby)
     */
    private Lobby getLobbyByLobbyID(UUID lobbyID) {
        return lobbyLiveMap.get(lobbyID);
    }

    /**
     * Get the executor who can perform actions on the game model, according to lobby id
     * @param lobbyID Id to look for
     * @return ExecutorService : game manager for this lobby
     */
    public ExecutorService getExecutorByLobbyID(UUID lobbyID){
        return lobbyLiveMap.get(lobbyID).getLobbyExecutorService();
    }

    /**
     * Get the player's unique nickName, based on the handler
     * @param playerHandler handler to find the nickName
     * @return  player nickName
     */
    public String getPlayerNickName(ClientHandler playerHandler){
        return this.playersHandlerMap.get(playerHandler);
    }

    /**
     * Informs if the server has reached the maximum number of ACTIVE lobbies running
     * @return true if the running lobbies have reached the maximum number
     */
    public boolean isReachedMaxNumLobbies(){
        return activeLobbies() >= this.maxNumLobbiesManaged;
    }

    /**
     * Returns the number of lobbies currently active on the server, those on hold are not counted
     * @return lobbies currently active
     */
    private int activeLobbies(){
        return (lobbyLiveMap.size()-this.waitingLobbies);
    }
}
