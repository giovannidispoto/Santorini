package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class SetReadyPlayerCommand implements Command {
    private String player;

    public SetReadyPlayerCommand(String player){
        this.player = player;
    }


    @Override
    public void execute(Controller controller, ClientHandler handler) {
        if(controller.checkHandler(player, handler) == false){
            throw new RuntimeException("Trying to operate like another player!");
        }
        controller.setPlayerReady(player);
    }
}
