package it.polimi.ingsw.server.actions.data;

public class WorkerPosition {
    private int workerID;
    private int x;
    private int y;

    public WorkerPosition(int workerID, int x, int y){
        this.workerID = workerID;
        this.x = x;
        this.y = y;
    }

    public int getWorkerID() {
        return workerID;
    }

    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }
}
