package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 * StartTurnCommand represent startTurn action from the client
 */
public class StartTurnCommand implements Command{
    private boolean basicTurn;
    private String playerNickname;
    private boolean result;

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
        result = true;
    }

    public boolean getResult(){
        return result;
    }
}
