package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

public class StartTurnInterface {
    private Boolean basicTurn;
    private String playerNickname;

    public StartTurnInterface(String playerNickname, Boolean basicTurn){
        this.basicTurn = basicTurn;
        this.playerNickname = playerNickname;
    }
}
