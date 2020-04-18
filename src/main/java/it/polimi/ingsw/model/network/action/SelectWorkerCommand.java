package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class SelectWorkerCommand implements Command{
    private String playerNickname;
    private int worker;

    /**
     *
     * @param playerNickname
     * @param worker
     */
    public SelectWorkerCommand(String playerNickname, int worker){
        this.playerNickname = playerNickname;
        this.worker = worker;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.selectWorker(playerNickname, worker);
    }
}
