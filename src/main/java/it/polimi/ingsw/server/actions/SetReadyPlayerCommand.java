package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * SetReadyPlayerCommand represent setReadyPlayer action from the client indicating that player is ready
 */
public class SetReadyPlayerCommand implements Command {
    private String playerNickame;

    /**
     * Create SetReadyPlayerCommand
     * @param playerNickname player
     */
    public SetReadyPlayerCommand(String playerNickname){
        this.playerNickame = playerNickname;
    }

    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        if(controller.checkHandler(playerNickame, handler) == false){
            throw new RuntimeException("Trying to operate like another player!");
        }
        controller.setPlayerReady(playerNickame);
    }
}
