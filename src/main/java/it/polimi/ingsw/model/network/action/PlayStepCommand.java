package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class PlayStepCommand implements Command {
    private int x;
    private int y;

    /**
     *
     * @param x
     * @param y
     */
    public PlayStepCommand(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.playStep(x,y);
    }
}
