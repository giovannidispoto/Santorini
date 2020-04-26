package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.network.actions.data.dataInterfaces.CellInterface;
import it.polimi.ingsw.client.controller.ClientController;


public class WorkerViewUpdateCommand implements Command {
    CellInterface[][] workerView;

    /**
     * Create command
     * @param battlefield battlefield
     */
    public WorkerViewUpdateCommand(CellInterface[][] battlefield) {
        this.workerView = battlefield;
    }

    @Override
    public void execute(ClientController clientController) {
        clientController.setWorkerView(this.workerView);
    }
}
