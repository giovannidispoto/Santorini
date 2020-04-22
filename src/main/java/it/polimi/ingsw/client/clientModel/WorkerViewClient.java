package it.polimi.ingsw.client.clientModel;

import it.polimi.ingsw.client.network.actions.data.CellInterface;

/**
 * Class used in update message of worker view
 */
public class WorkerViewClient {
    private CellInterface[][] workerView;
    private static WorkerViewClient instance = null;

    /**
     * Factory method that returns the Battlefield instance (Singleton)
     * @return Battlefield object
     */
    public static WorkerViewClient getWorkerViewInstance(){
        if(instance == null)
            instance = new WorkerViewClient();
        return instance;
    }

    public void setWorkerView(CellInterface[][] workerView) {
        this.workerView = workerView;
    }

    public CellInterface getCell(int x, int y){
        return workerView[x][y];
    }
}
