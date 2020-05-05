package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.Step;

/** Start Turn command, selecting if player want a basic or special turn*/
public class SetStartTurnCommand implements Command {
    private String playerNickname;
    private boolean basicTurn;
    private Step currentStep;

    public SetStartTurnCommand(String playerNickname, boolean basicTurn) {
        this.playerNickname = playerNickname;
        this.basicTurn = basicTurn;
    }

    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.startTurn(playerNickname, basicTurn);
        this.currentStep = controller.getStepState();
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("setStartTurnResponse", this)));
        handler.sendMessageQueue();
    }
}
