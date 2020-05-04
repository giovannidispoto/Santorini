package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.CellInterface;

/**
 * SelectWorkerCommand represent selectWorker action from the client
 */
public class SelectWorkerCommand implements Command{
    private String playerNickname;
    private int x;
    private int y;
    private boolean[][] workerView;

    /**
     * Create SelectWorkerCommand
     * @param playerNickname player
     * @param x row
     * @param y column
     */
    public SelectWorkerCommand(String playerNickname, int x, int y){
        this.playerNickname = playerNickname;
        this.x = x;
        this.y = y;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        this.workerView = controller.selectWorker(playerNickname,x,y);
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("selectWorkerResponse", this)));
        handler.sendMessageQueue();
    }

}
