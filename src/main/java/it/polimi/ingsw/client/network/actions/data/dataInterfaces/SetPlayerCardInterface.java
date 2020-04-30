package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

public class SetPlayerCardInterface {
    String playerNickname;
    String card;

    public SetPlayerCardInterface(String playerNickname, String card) {
        this.playerNickname = playerNickname;
        this.card = card;
    }
}
