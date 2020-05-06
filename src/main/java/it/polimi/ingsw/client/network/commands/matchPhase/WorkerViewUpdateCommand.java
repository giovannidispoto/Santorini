package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.commands.Command;


public class WorkerViewUpdateCommand implements Command {
    boolean[][] workerView;

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkerView(this.workerView);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitWorkerViewUpdate){
            clientController.waitManager.waitWorkerViewUpdate.setUsed();
            clientController.waitManager.waitWorkerViewUpdate.notify();
        }
    }
}
