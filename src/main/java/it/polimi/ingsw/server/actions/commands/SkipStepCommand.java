package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

/**
 * SkipStep command
 */
public class SkipStepCommand implements Command{
    /*Send back to client currentStep*/
    private Step currentStep;

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        this.currentStep = controller.skipStep();
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("skipStepResponse", this)));
        handler.sendMessageQueue();
    }

}
