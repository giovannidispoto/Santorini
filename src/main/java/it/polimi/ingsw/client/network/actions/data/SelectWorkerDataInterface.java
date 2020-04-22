package it.polimi.ingsw.client.network.actions.data;

public class SelectWorkerDataInterface {
    private String playerNickname;
    private int workerID;

    public SelectWorkerDataInterface(String playerNickname, int workerID){
        this.playerNickname = playerNickname;
        this.workerID = workerID;
    }
}
