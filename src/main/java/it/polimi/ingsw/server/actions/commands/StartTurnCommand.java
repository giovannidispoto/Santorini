package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
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
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("startTurnResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getResult(){
        return result;
    }
}
