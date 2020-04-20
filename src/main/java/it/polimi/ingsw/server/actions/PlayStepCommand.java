package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * PlayStepCommand represent playStp action from the client
 */
public class PlayStepCommand implements Command {
    private int x;
    private int y;

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
        controller.playStep(x,y);
    }
}
