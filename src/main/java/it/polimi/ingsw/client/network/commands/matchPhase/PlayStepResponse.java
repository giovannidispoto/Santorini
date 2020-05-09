package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

public class PlayStepResponse implements Command {
    int x;
    int y;
    Step nextStep;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCurrentStep(this.nextStep);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitPlayStepResponse){
            WaitManager.waitPlayStepResponse.setUsed();
            WaitManager.waitPlayStepResponse.notify();
        }
    }
}
