package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.WorkerPosition;

import java.util.List;

/**
 * SetWorkerPosition class represent setWorkerPosition action that
 * collocates worker on the battlefield
 */
public class SetWorkersPosition implements Command{
    private String playerNickname;
    private List<WorkerPosition> workersPosition;

    /**
     * Create setWorkerPosition Command
     * @param playerNickname player
     * @param workersPosition position data
     * */
    public SetWorkersPosition(String playerNickname, List<WorkerPosition> workersPosition) {
        this.playerNickname = playerNickname;
        this.workersPosition = workersPosition;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {

        controller.setInitialWorkerPosition(playerNickname,
                                    workersPosition.get(0).getWorkerID(), workersPosition.get(0).getX(), workersPosition.get(0).getY(),
                                    workersPosition.get(1).getWorkerID(), workersPosition.get(1).getX(), workersPosition.get(1).getY());

        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("setWorkerPositionResponse", this)));
        handler.sendMessageQueue();
    }
}
