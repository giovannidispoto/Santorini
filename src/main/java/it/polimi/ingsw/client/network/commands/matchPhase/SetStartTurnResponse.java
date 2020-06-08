package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

/**
 * Class that manages the response: SetStartTurn
 */
public class SetStartTurnResponse implements Command {
    String playerNickname;
    boolean basicTurn;
    Step currentStep;

    public SetStartTurnResponse(String playerNickname, boolean basicTurn, Step currentStep) {
        this.playerNickname = playerNickname;
        this.basicTurn = basicTurn;
        this.currentStep = currentStep;
    }


    @Override
    public void execute(ClientController clientController) {
        clientController.setCurrentStep(this.currentStep);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitStartTurn){
            WaitManager.waitStartTurn.setUsed();
            WaitManager.waitStartTurn.notify();
        }
    }
}
