package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

/**
 * Class used in update command of battlefield and worker view
 * Needed for Deserialization
 */
public class WorkerViewInterface {
    private CellInterface[][] workerView;

    /**
     * Create new Message Interface
     * @param workerView data
     */
    public WorkerViewInterface(CellInterface[][] workerView){
        this.workerView = workerView;
    }

    //return board of data interface
    public CellInterface[][] getWorkerView() {
        return workerView;
    }
}
