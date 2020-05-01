package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.CellInterface;

public class GetBattlefield implements Command{
    private CellInterface[][] battlefield;
    private boolean result;

    public GetBattlefield(){
        result = false;
    }

    @Override
    public void execute(Controller controller, ClientHandler handler) {
        battlefield = controller.getBattlefield();
        result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("getBattlefieldResponse", this)));
        handler.sendMessageQueue();
    }
}
