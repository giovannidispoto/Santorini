package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.network.actions.data.dataInterfaces.CellInterface;
import it.polimi.ingsw.client.clientModel.WorkerViewClient;
import it.polimi.ingsw.client.controller.Controller;


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
    public void execute(Controller controller) {
        WorkerViewClient.getWorkerViewInstance().setWorkerView(workerView);
    }
}
