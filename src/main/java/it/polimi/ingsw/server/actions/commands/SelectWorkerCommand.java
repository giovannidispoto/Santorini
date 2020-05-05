package it.polimi.ingsw.server.actions.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * SelectWorkerCommand represent selectWorker action from the client
 */
public class SelectWorkerCommand implements Command{
    private String playerNickname;
    private int x;
    private int y;
   // private boolean[][] workerView;

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
        controller.selectWorker(playerNickname,handler,x,y);
       // handler.responseQueue(new Gson().toJson(new BasicMessageInterface("selectWorkerResponse", this)));
        handler.sendMessageQueue();
    }

}
