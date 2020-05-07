package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

public class WorkerPositionInterface {
    private int workerID;
    private int x;
    private int y;


    public WorkerPositionInterface(int workerID, int x, int y) {
        this.workerID = workerID;
        this.x = x;
        this.y = y;
    }
}
