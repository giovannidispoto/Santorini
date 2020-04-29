package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

/**
 * GetWorkersIDCommand represent getWorkers action from the client
 */
public class GetWorkersIDCommand implements Command{
    private String player;
    private boolean result;
    private List<Integer> workers;

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

        workers = controller.getWorkersId(player);
        result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("getWorkersIDResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getStatus(){
        return result;
    }

    public List<Integer> getWorkers(){
        return List.copyOf(workers);
    }
}
