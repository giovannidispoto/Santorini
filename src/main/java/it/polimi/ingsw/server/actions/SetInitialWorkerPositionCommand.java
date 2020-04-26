package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * SetInitialWorkerPosition represent setWorkerPosition action from the client
 */
public class SetInitialWorkerPositionCommand implements Command{
    private String player;
    private int worker;
    private int x;
    private int y;
    private boolean result;

    /**
     * Create SetInitialWorkerPositionCommand
     * @param player player
     * @param worker worker
     * @param x row
     * @param y column
     */
    public SetInitialWorkerPositionCommand(String player, int worker, int x, int y){
        this.player = player;
        this.worker = worker;
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.setInitialWorkerPosition(player, worker, x,y);
        result = true;
        handler.response(new Gson().toJson(new BasicMessageInterface("setInitialWorkerResponse", this)));
    }

    public boolean getResult(){
        return result;
    }
}
