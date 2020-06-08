package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

/**
 * Allows you to send the card chosen by the player to the server
 */
public class SetPlayerCardInterface {
    String playerNickname;
    String card;

    /**
     * Create new data interface
     * @param playerNickname nickName
     * @param card  chosen card
     */
    public SetPlayerCardInterface(String playerNickname, String card) {
        this.playerNickname = playerNickname;
        this.card = card;
    }
}
