package it.polimi.ingsw.model;

/**
 * */
public class Worker {
    private final Player ownerWorker;
    private final Color workerColor;
    private int rowWorker;
    private int colWorker;
    private Cell[][] workerView;

    /**
     *
     * @param ownerWorker
     * @param workerColor
     */
    public Worker(Player ownerWorker, Color workerColor) {
        this.ownerWorker = ownerWorker;
        this.workerColor = workerColor;

    }
    /**
     *
     * @return
     */
    public Player getOwnerWorker() {
        return ownerWorker;
    }

    /**
     *
     * @return
     */
    public Color getWorkerColor() {
        return workerColor;
    }

    /**
     *
     * @return
     */
    public int getRowWorker() {
        return rowWorker;
    }

    /**
     *
     * @return
     */
    public int getColWorker() {
        return colWorker;
    }

    public void changeWorkerPosition(int newRowWorker, int newColWorker){
        this.rowWorker = newRowWorker;
        this.colWorker = newColWorker;
        Battlefield.getBattelfieldInstance().updateWorkerPosition(this, newRowWorker, newColWorker);
    }
}
