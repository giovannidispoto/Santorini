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
    private boolean result;
    private List<Integer> workersID;

    public GetWorkersIDCommand(String player){
        this.player = player;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        if(controller.checkHandler(player, handler) == false){
            throw new RuntimeException("Trying to operate like another player!");
        }

        workersID = controller.getWorkersId(player);
        result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("getWorkersIDResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getStatus(){
        return result;
    }

    public List<Integer> getWorkers(){
        return List.copyOf(workersID);
    }
}
