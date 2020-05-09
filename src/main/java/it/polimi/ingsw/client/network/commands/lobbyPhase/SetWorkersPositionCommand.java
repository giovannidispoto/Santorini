package it.polimi.ingsw.client.network.commands.lobbyPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;

import java.util.List;

public class SetWorkersPositionCommand implements Command {
    List<Integer> workersID;

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkersID(workersID);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitSetWorkersPosition){
            WaitManager.waitSetWorkersPosition.setUsed();
            WaitManager.waitSetWorkersPosition.notify();
        }
    }
}
