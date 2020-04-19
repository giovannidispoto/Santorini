package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * StartTurnCommand represent startTurn action from the client
 */
public class StartTurnCommand implements Command{
    private boolean basicTurn;
    private String playerNickname;

    /**
     * Create StartTurnCommand
     * @param basicTurn true if the player want a basic turn, false otherwise
     * @param playerNickname player
     */
    public StartTurnCommand(boolean basicTurn, String playerNickname){
        this.basicTurn = basicTurn;
        this.playerNickname = playerNickname;
    }

    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.startTurn(basicTurn,playerNickname);
    }
}
