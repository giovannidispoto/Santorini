package it.polimi.ingsw.server.actions.data;

public class SetPickedCardRequest {
    private String playerNickname;

    public SetPickedCardRequest(String playerNickname){
        this.playerNickname = playerNickname;
    }
}
