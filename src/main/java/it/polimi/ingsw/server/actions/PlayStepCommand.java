package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;

/**
 * PlayStepCommand represent playStp action from the client
 */
public class PlayStepCommand implements Command {
    private int x;
    private int y;
    private boolean result;
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
        this.result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("playStepResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getResult(){
        return result;
    }

    public Step getNextStep(){
        return nextStep;
    }
}
