package it.polimi.ingsw.client.network.data.dataInterfaces;

public class SetPlayerCardInterface {
    String playerNickname;
    String card;

    public SetPlayerCardInterface(String playerNickname, String card) {
        this.playerNickname = playerNickname;
        this.card = card;
    }
}
