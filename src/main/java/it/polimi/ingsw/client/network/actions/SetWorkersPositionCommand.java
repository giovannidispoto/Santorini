package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.List;

public class SetWorkersPositionCommand implements Command{
    List<Integer> workersID;

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkersID(workersID);
        //Awakens who was waiting Server Response
        synchronized (clientController.waitManager.waitSetWorkersPosition){
            clientController.waitManager.waitSetWorkersPosition.setUsed();
            clientController.waitManager.waitSetWorkersPosition.notify();
        }
    }
}
