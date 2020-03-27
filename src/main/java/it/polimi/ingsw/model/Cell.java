package it.polimi.ingsw.model;

/**
 * The Cell class represent the cell inside the battelfield
 */
public class Cell {
    /* Tower in the cell, defualt to ground level*/
    private Tower cellTower;
    private Worker cellWorker;

    /**
     *
     * @param tower
     */
    public Cell(Tower tower){
        this.cellTower = tower;
        this.cellWorker = null;
    }

    /**
     *
     * @return
     */
    public Tower getTower(){
        return this.cellTower;
    }

    /**
     *
     * @return
     */
    public boolean isWorkerPresent(){
        return this.cellWorker != null;
    }

    /**
     *
     * @param worker
     */
    public void setWorker(Worker worker){
        this.cellWorker = worker;
    }

    public Worker getWorker(){return this.cellWorker;}

    /**
     *
     */
    public void removeWorker(){
        this.cellWorker = null;
    }
}
