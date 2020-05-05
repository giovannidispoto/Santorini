package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.network.commands.Command;
import it.polimi.ingsw.client.network.data.dataInterfaces.CellInterface;
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
