package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.List;

/**
 * GetWorkersIDCommand represent getWorkersID action returned by server
 */
public class SetWorkersIDCommand implements Command{
    List<Integer> workersID;

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkersID(workersID);
        //Awakens who was waiting Server Response
        synchronized (clientController.lockManager.lockSetWorkersID){
            clientController.lockManager.lockSetWorkersID.setUsed();
            clientController.lockManager.lockSetWorkersID.notify();
        }
    }
}
