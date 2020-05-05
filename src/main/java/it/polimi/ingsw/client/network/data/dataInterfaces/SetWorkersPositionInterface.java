package it.polimi.ingsw.client.network.data.dataInterfaces;

import java.util.List;

public class SetWorkersPositionInterface {
    private String playerNickname;
    private List<WorkerPositionInterface> workersPosition;

    public SetWorkersPositionInterface(String playerNickname, List<WorkerPositionInterface> workersPosition){
        this.playerNickname = playerNickname;
        this.workersPosition = workersPosition;
    }
}
