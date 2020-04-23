package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

import it.polimi.ingsw.client.clientModel.basic.Color;

public class AddPlayerInterface {
    private String playerNickname;
    private Color playerColor;

    public AddPlayerInterface(String playerNickname, Color playerColor){
        this.playerNickname = playerNickname;
        this.playerColor = playerColor;
    }
}
