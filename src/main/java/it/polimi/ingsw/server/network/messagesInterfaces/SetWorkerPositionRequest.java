package it.polimi.ingsw.server.network.messagesInterfaces;

import java.util.List;

/**
 * SetWorkerPositionRequest class represent format of request sent to client
 */
public class SetWorkerPositionRequest {
    List<Integer> workersID;

    /**
     * Create new request
     * @param workersID workers ID
     */
    public SetWorkerPositionRequest(List<Integer> workersID){
        this.workersID = workersID;
    }
}
