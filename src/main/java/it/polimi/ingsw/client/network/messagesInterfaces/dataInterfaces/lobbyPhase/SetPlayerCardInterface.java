package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

public class SetPlayerCardInterface {
    String playerNickname;
    String card;

    public SetPlayerCardInterface(String playerNickname, String card) {
        this.playerNickname = playerNickname;
        this.card = card;
    }
}
