package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

public class SetStartTurnResponse implements Command {
    private String playerNickname;
    private boolean basicTurn;
    private Step currentStep;

    public SetStartTurnResponse(String playerNickname, boolean basicTurn, Step currentStep) {
        this.playerNickname = playerNickname;
        this.basicTurn = basicTurn;
        this.currentStep = currentStep;
    }


    @Override
    public void execute(ClientController clientController) {
        clientController.setCurrentStep(this.currentStep);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitStartTurn){
            clientController.waitManager.waitStartTurn.setUsed();
            clientController.waitManager.waitStartTurn.notify();
        }
    }
}
