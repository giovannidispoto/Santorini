package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.actions.data.WorkersIDInterface;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

/**
 * GetWorkersIDCommand represent getWorkers action from the client
 */
public class GetWorkersIDCommand implements Command{
    private String player;

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
        List<Integer> workers = controller.getWorkersId(player);
        Gson gson = new Gson();
        handler.response(gson.toJson(new WorkersIDInterface("getWorkersID", workers)));
    }
}
