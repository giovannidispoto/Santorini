package it.polimi.ingsw.client.network.data.dataInterfaces;

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
