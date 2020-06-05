package it.polimi.ingsw.server.network.messagesInterfaces;

import java.util.List;

public class SetWorkerPositionRequest {
    List<Integer> workersID;

    public SetWorkerPositionRequest(List<Integer> workersID){
        this.workersID = workersID;
    }
}
