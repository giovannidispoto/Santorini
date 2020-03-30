package it.polimi.ingsw.model;

/**
 * Class Cell represents a single cell of the board
 */
public class Cell {

    private Tower cellTower;
    private Worker cellWorker;

    /**
     * Class Constructor
     * @param tower inside the cell (the default value is GROUND)
     */
    public Cell(Tower tower){
        this.cellTower = tower;
        this.cellWorker = null;
    }

    /**
     * Looks in there is a worker in the cell
     * @return boolean value about the presence of a worker in  the cell
     */
    public boolean isWorkerPresent(){
        return this.cellWorker != null;
    }

    /**
     * Removes a worker from the cell
     */
    public void removeWorker(){
        this.cellWorker = null;
    }

    /**
     * Gets the tower inside the cell
     * @return Tower object
     */
    public Tower getTower(){
        return this.cellTower;
    }

    public Worker getWorker(){return this.cellWorker;}

    /**
     * Sets a worker in the cell
     * @param worker subject
     */
    public void setWorker(Worker worker){
        this.cellWorker = worker;
    }
}
