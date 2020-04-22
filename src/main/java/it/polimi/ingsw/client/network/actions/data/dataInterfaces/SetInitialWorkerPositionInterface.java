package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

public class SetInitialWorkerPositionInterface {
    private String playerNickname;
    private int worker;
    private int x;
    private int y;

    public SetInitialWorkerPositionInterface(String playerNickname, int workerID, int row, int col){
        this.playerNickname = playerNickname;
        this.worker = workerID;
        this.x = row;
        this.y = col;
    }
}
