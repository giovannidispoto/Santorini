package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.List;

/**
 * GetWorkersIDCommand represent getWorkersID action returned by server
 */
public class GetWorkersIDCommand implements Command{
    List<Integer> workersID;

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkersID(workersID);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockGetWorkersID){
            clientController.lockManager.lockGetWorkersID.setUsed();
            clientController.lockManager.lockGetWorkersID.notify();
        }
    }
}
