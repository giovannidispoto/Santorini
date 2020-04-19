package it.polimi.ingsw.model;

import it.polimi.ingsw.model.network.action.data.CellInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Worker class represents a pawn of the game
 */
public class Worker implements Subject{
    private final Player ownerWorker;
    private static int count = 0;
    private int id;
    private final Color workerColor;
    private int rowWorker;
    private int colWorker;
    private Cell[][] workerView;
    private List<Observer> observers;

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
        notifyUpdate();
    }

    /**
     *
     * @return
     */
    public Cell[][] getWorkerView(){
        return this.workerView;
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
    public void attach(Observer o) {
        observers.add(o);
    }

    /**
     * Remove Observer
     * @param o
     */
    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    /**
     * Notify observer that matrix is changed
     */
    @Override
    public void notifyUpdate() {
        CellInterface[][] workerView = new CellInterface[Battlefield.N_ROWS_VIEW][Battlefield.N_COLUMNS_VIEW];
        String player;
        Color workerColor;
       // Battlefield instance = Battlefield.getBattlefieldInstance();
        for(int i = 0; i < Battlefield.N_ROWS_VIEW; i++){
            for(int j = 0; j < Battlefield.N_COLUMNS_VIEW; j++){
                if(this.workerView[i][j] != null) {
                    player = this.workerView[i][j].getWorker() != null ? this.workerView[i][j].getWorker().getOwnerWorker().getPlayerNickname():null;
                    workerColor = player != null ? this.workerView[i][j].getWorker().getOwnerWorker().getPlayerColor(): null;
                    workerView[i][j] = new CellInterface(player, workerColor, this.workerView[i][j].getTower().getHeight(),this.workerView[i][j].getTower().getLastBlock());
                }
            }
        }
        for(Observer o: observers)
            o.update(workerView, Message.WORKERVIEW);
    }

    /**
     * Gets worker id
     * @return
     */
    public int getId() {
        return id;
    }
}
