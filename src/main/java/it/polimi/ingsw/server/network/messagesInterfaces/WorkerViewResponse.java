package it.polimi.ingsw.server.network.messagesInterfaces;

/**
 * Interface used for sending update about WorkerView
 * */

public class WorkerViewResponse {
    private boolean workerView[][];

    public WorkerViewResponse(boolean [][] workerView){
        this.workerView = workerView;
    }
}
