package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

/**
 *
 */
public class SkipStepCommand implements Command{
    private boolean result;

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.skipStep();
        result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("skipStepResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getResult(){
        return result;
    }
}
