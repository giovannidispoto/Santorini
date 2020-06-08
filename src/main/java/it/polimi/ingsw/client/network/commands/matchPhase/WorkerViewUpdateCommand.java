package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

/**
 * Class that manages the response: WorkerViewUpdate
 */
public class WorkerViewUpdateCommand implements Command {
    boolean[][] workerView;

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkerView(this.workerView);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitWorkerViewUpdate){
            WaitManager.waitWorkerViewUpdate.setUsed();
            WaitManager.waitWorkerViewUpdate.notify();
        }
    }
}
