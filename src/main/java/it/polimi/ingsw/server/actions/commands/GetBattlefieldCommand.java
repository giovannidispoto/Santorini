package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.CellInterface;

/**
 * GetBattlefieldCommand class represent getBattlefield action from the client
* */
public class GetBattlefieldCommand implements Command{
    private CellInterface[][] cellMatrix;

    /**
     * Execute command on the server
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        cellMatrix = controller.getBattlefield();
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("getBattlefieldResponse", this)));
        handler.sendMessageQueue();
    }
}
