package it.polimi.ingsw.server.actions.data;

/**
 * Interface used for sending update about WorkerView
 * */

public class WorkerViewResponse {
    private boolean workerView[][];

    public WorkerViewResponse(boolean [][] workerView){
        this.workerView = workerView;
    }
}
