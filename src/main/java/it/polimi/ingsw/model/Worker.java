package it.polimi.ingsw.model;

import it.polimi.ingsw.server.observers.ObserverWorkerView;
import it.polimi.ingsw.server.observers.SubjectWorkerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Worker class represents a pawn of the game
 */
public class Worker implements SubjectWorkerView {
    private final Player ownerWorker;
    private static int count = 0;
    private int id;
    private final Color workerColor;
    private int rowWorker;
    private int colWorker;
    private Cell[][] workerView;
    private List<ObserverWorkerView> observers;

    /**
     * Class Constructor
     * @param workerOwner The player who owns the pawn
     */
    public Worker(Player workerOwner) {
        this.ownerWorker = workerOwner;
        this.workerColor = workerOwner.getPlayerColor();
        observers = new ArrayList<>();
        this.id = count++;
    }

    /**
     * Updates the worker position
     * @param newRowWorker new row worker
     * @param newColWorker new col worker
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
     *Set worker view
     * @param workerView worker view
     */
    public void setWorkerView(Cell[][] workerView){
        this.workerView = workerView;
        //call observer

    }

    /**
     * Gets WorkerView to requester
     * @return worker view
     */
    public Cell[][] getWorkerView(){
        return this.workerView;
    }

    /** Check if the workerView is all null and therefore no action is possible
     *
     * @return  true if workerView is all null, false at least one action is possible
     */
    public boolean isInvalidWorkerView(){
        for(int x=0; x < Battlefield.N_ROWS; x++){
            for(int y=0; y < Battlefield.N_COLUMNS; y++){
                if(workerView[x][y] != null){
                    return false;
                }
            }
        }
        return true;
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

    /**
     * Attach new Observer
     * @param o Observer
     */
    @Override
    public void attach(ObserverWorkerView o) {
        observers.add(o);
    }

    /**
     * Remove Observer
     * @param o observer
     */
    @Override
    public void detach(ObserverWorkerView o) {
        observers.remove(o);
    }

    /**
     * Remove all observers
     */
    @Override
    public void detachAll() {
        observers = new ArrayList<>();
    }

    /**
     * Notify observer that matrix is changed
     */
    @Override
    public void notifyUpdate() {
        boolean[][] workerView = new boolean [Battlefield.N_ROWS_VIEW][Battlefield.N_COLUMNS_VIEW];
        String player;
        Color workerColor;
       // Battlefield instance = Battlefield.getBattlefieldInstance();
        for(int i = 0; i < Battlefield.N_ROWS_VIEW; i++){
            for(int j = 0; j < Battlefield.N_COLUMNS_VIEW; j++){
                workerView[i][j] = this.workerView[i][j] != null;
            }
        }
        for(ObserverWorkerView o: observers)
            o.update(workerView);
    }

    /**
     * Gets worker id
     * @return worker id
     */
    public int getId() {
        return id;
    }
}
