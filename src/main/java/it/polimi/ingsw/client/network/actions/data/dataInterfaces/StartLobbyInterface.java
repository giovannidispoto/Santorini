package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

public class StartLobbyInterface {
    private String playerNickname;
    private int lobbySize;

    public StartLobbyInterface(String playerNickname, int lobbySize){
        this.playerNickname = playerNickname;
        this.lobbySize = lobbySize;
    }
}
