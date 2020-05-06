package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

/**
 * PlayStepCommand represent playStp action from the client
 */
public class PlayStepCommand implements Command {
    private int x;
    private int y;
    private Step nextStep;

    /**
     * Create new PlayStep
     * @param x row
     * @param y column
     */
    public PlayStepCommand(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        nextStep = controller.playStep(x,y);
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("playStepResponse", this)));
        handler.sendMessageQueue();
    }

}
