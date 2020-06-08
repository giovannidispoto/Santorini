package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

/**
 * Allows you to enter the data necessary to represent a worker selection
 */
public class WorkerPositionInterface {
    int workerID;
    int x;
    int y;

    /**
     * Create new data interface
     * @param workerID id assigned by the server
     * @param row battlefield row
     * @param col battlefield column
     */
    public WorkerPositionInterface(int workerID, int row, int col) {
        this.workerID = workerID;
        this.x = row;
        this.y = col;
    }
}
