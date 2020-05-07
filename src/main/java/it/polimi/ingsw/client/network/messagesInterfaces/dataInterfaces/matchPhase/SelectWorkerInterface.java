package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase;

public class SelectWorkerInterface {
    String playerNickname;
    int x;
    int y;

    public SelectWorkerInterface(String playerNickname, int x, int y){
        this.playerNickname = playerNickname;
        this.x = x;
        this.y = y;
    }
}
