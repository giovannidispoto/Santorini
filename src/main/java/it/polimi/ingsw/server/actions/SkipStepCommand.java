package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

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
    }

    public boolean getResult(){
        return result;
    }
}
