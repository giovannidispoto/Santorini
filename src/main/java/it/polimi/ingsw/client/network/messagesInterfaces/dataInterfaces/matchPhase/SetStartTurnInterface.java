package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase;

/**
 * Allows you to send the turn choice to the server
 */
public class SetStartTurnInterface {
    String playerNickname;
    boolean basicTurn;

    /**
     * Create new data interface
     * @param playerNickname nickName
     * @param basicTurn true = make only a basic turn (no effects), false = use effects
     */
    public SetStartTurnInterface(String playerNickname, boolean basicTurn) {
        this.playerNickname = playerNickname;
        this.basicTurn = basicTurn;
    }
}
