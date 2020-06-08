package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.clientModel.basic.SelectedWorker;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

/**
 * Class that manages the response: PlayStep
 */
public class PlayStepResponse implements Command {
    int x;
    int y;
    Step nextStep;

    @Override
    public void execute(ClientController clientController) {
        clientController.setCurrentStep(this.nextStep);
        //If the turn is Ended the current worker position is no longer valid
        if(nextStep == Step.END) {
            clientController.setCurrentWorker(null);
        }else {
            clientController.setCurrentWorker(new SelectedWorker(x, y));
        }
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitPlayStepResponse){
            WaitManager.waitPlayStepResponse.setUsed();
            WaitManager.waitPlayStepResponse.notify();
        }
    }
}
