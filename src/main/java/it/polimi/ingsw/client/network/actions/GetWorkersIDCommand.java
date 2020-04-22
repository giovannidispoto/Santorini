package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.controller.ClientController;

import java.util.List;

/**
 * GetWorkersIDCommand represent getWorkersID action returned by server
 */
public class GetWorkersIDCommand implements Command{
    private final List<Integer> workersID;

    /**
     * Create command
     * @param workersID client workersID
     */
    public GetWorkersIDCommand(List<Integer> workersID) {
        this.workersID = workersID;
    }

    /*
     * Execute command
     */
    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkersID(workersID);
    }
}
