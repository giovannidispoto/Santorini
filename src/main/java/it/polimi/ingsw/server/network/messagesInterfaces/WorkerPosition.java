package it.polimi.ingsw.server.network.messagesInterfaces;
/*
* Interface used for receiving position of the worker on the battlefield
* */
public class WorkerPosition {
    private int workerID;
    private int x;
    private int y;

    /**
     * Create new WorkerPosition
     * @param workerID worker ID
     * @param x x
     * @param y y
     */
    public WorkerPosition(int workerID, int x, int y){
        this.workerID = workerID;
        this.x = x;
        this.y = y;
    }

    /**
     * Gets worker ID
     * @return worker ID
     */
    public int getWorkerID() {
        return workerID;
    }

    /**
     * Gets X coordinate
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets y coordinate
     * @return y coordinate
     */
    public int getY(){
        return y;
    }
}
