package it.polimi.ingsw.server.actions.data;

/**Server request to the client*/
public class SetPickedCardRequest {
    private String playerNickname;

    public SetPickedCardRequest(String playerNickname){
        this.playerNickname = playerNickname;
    }
}
