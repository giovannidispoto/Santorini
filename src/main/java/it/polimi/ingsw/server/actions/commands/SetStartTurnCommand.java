package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

/**
 * Start Turn command, selecting if player want a basic or special turn
 * */
public class SetStartTurnCommand implements Command {
    private String playerNickname;
    private boolean basicTurn;
    /*Current step sent to client, used to start action on the turn*/
    private Step currentStep;

    /**
     * Create command
     * @param playerNickname playerNickname
     * @param basicTurn basic turn if true, special if false
     */
    public SetStartTurnCommand(String playerNickname, boolean basicTurn) {
        this.playerNickname = playerNickname;
        this.basicTurn = basicTurn;
    }

    /**
     * Execute command on the server
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.startTurn(playerNickname, basicTurn);
        this.currentStep = controller.getStepState();
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("setStartTurnResponse", this)));
        handler.sendMessageQueue();
    }
}
