package it.polimi.ingsw.server.actions.data;

import java.util.List;

public class SetWorkerPositionRequest {
    List<Integer> workersID;

    public SetWorkerPositionRequest(List<Integer> workersID){
        this.workersID = workersID;
    }
}
