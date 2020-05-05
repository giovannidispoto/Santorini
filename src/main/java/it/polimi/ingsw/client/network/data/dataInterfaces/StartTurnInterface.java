package it.polimi.ingsw.client.network.data.dataInterfaces;

public class StartTurnInterface {
    private Boolean basicTurn;
    private String playerNickname;

    public StartTurnInterface(String playerNickname, Boolean basicTurn){
        this.basicTurn = basicTurn;
        this.playerNickname = playerNickname;
    }
}
