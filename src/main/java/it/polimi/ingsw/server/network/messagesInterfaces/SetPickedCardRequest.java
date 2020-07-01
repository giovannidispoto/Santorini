package it.polimi.ingsw.server.network.messagesInterfaces;

/**
 * SetPickedCardRequest class represent format of request sent to client
 * */
public class SetPickedCardRequest {
    private String playerNickname;

    /**
     * Create new request
     * @param playerNickname player nickname
     */
    public SetPickedCardRequest(String playerNickname){
        this.playerNickname = playerNickname;
    }
}
