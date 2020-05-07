package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase;

public class SetStartTurnInterface {
    String playerNickname;
    boolean basicTurn;


    public SetStartTurnInterface(String playerNickname, boolean basicTurn) {
        this.playerNickname = playerNickname;
        this.basicTurn = basicTurn;
    }
}
