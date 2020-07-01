package it.polimi.ingsw.server.network.messagesInterfaces;

/**
 * ActualPlayerResponse represent format of actualPlayer message
 */
public class ActualPlayerResponse {
    private String playerNickname;

    /**
     * Create new message
     * @param playerNickname actual player nickname
     */
    public ActualPlayerResponse(String playerNickname){
        this.playerNickname = playerNickname;
    }
}
