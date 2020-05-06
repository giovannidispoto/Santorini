package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

import java.util.List;

/**
 * GetWorkersIDCommand represent getWorkers action from the client
 */
public class GetWorkersIDCommand implements Command{
    private String player;
    private List<Integer> workersID;

    public GetWorkersIDCommand(String player){
        this.player = player;
    }

    /**
     * Execute command on the server
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        //check if handler is associated with playerNickname
        if(controller.checkHandler(player, handler)){
            workersID = controller.getWorkersId(player);
            handler.responseQueue(new Gson().toJson(new BasicMessageResponse("getWorkersIDResponse", this)));
            handler.sendMessageQueue();
        }
    }

}
