package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;

public class SkipStepResponse implements Command {
    Step currentStep;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCurrentStep(this.currentStep);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitSkipStepResponse){
            clientController.waitManager.waitSkipStepResponse.setUsed();
            clientController.waitManager.waitSkipStepResponse.notify();
        }
    }
}
