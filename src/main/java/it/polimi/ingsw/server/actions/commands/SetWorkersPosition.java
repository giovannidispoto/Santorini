package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.WorkerPosition;

import java.util.List;

public class SetWorkersPosition implements Command{
    private String playerNickname;
    private List<WorkerPosition> workersPosition;
    private boolean result;

    /**
     * Create setWorkerPosition Command
     * @param playerNickname player
     * @param workersPosition position data
     * */
    public SetWorkersPosition(String playerNickname, List<WorkerPosition> workersPosition) {
        this.playerNickname = playerNickname;
        this.workersPosition = workersPosition;
        this.result = false;
    }

    /*Execute command*/
    @Override
    public void execute(Controller controller, ClientHandler handler) {

        controller.setInitialWorkerPosition(playerNickname,
                                    workersPosition.get(0).getWorkerID(), workersPosition.get(0).getX(), workersPosition.get(0).getY(),
                                    workersPosition.get(1).getWorkerID(), workersPosition.get(1).getX(), workersPosition.get(1).getY());

        result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("setWorkerPositionResponse", this)));
        handler.sendMessageQueue();
    }
}
