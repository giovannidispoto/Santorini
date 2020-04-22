package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

public class SelectWorkerInterface {
    private String playerNickname;
    private int worker;

    public SelectWorkerInterface(String playerNickname, int workerID){
        this.playerNickname = playerNickname;
        this.worker = workerID;
    }
}
