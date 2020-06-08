package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

import java.util.List;

/**
 * Allows you to send the list containing the position of all workers of the player
 */
public class SetWorkersPositionInterface {
    String playerNickname;
    List<WorkerPositionInterface> workersPosition;

    /**
     * Create new data interface
     * @param playerNickname nickName
     * @param workersPosition   list containing the positions of the workers
     */
    public SetWorkersPositionInterface(String playerNickname, List<WorkerPositionInterface> workersPosition){
        this.playerNickname = playerNickname;
        this.workersPosition = workersPosition;
    }
}
