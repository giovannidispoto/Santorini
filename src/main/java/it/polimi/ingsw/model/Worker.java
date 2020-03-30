package it.polimi.ingsw.model;

/**
 * Worker class represents a pawn of the game
 */
public class Worker {
    private final Player ownerWorker;
    private final Color workerColor;
    private int rowWorker;
    private int colWorker;
    private Cell[][] workerView;

    /**
     * Class Constructor
     * @param workerOwner The player who owns the pawn
     */
    public Worker(Player workerOwner) {
        this.ownerWorker = workerOwner;
        this.workerColor = workerOwner.getPlayerColor();

    }

    /**
     * Updates the worker position
     */
    public void changeWorkerPosition(int newRowWorker, int newColWorker){
        Battlefield.getBattlefieldInstance().updateWorkerPosition(this, rowWorker, colWorker, newRowWorker, newColWorker);
        this.rowWorker = newRowWorker;
        this.colWorker = newColWorker;
    }

    /**
     * Gets the owner of the worker
     * @return Player object
     */
    public Player getOwnerWorker() {
        return ownerWorker;
    }

    /**
     * Gets the color associated with the worker
     * @return Color object
     */
    public Color getWorkerColor() {
        return workerColor;
    }

    /**
     * Gets the x coordinate of the worker inside the ground
     * @return integer
     */
    public int getRowWorker() {
        return rowWorker;
    }

    /**
     * Gets the y coordinate of the worker inside the ground
     * @return integer
     */
    public int getColWorker() {
        return colWorker;
    }

    public void setWorkerPosition(int rowWorker, int colWorker){
        this.rowWorker = rowWorker;
        this.colWorker = colWorker;
        Battlefield.getBattlefieldInstance().updateWorkerPosition(this, rowWorker, colWorker);
    }
}
