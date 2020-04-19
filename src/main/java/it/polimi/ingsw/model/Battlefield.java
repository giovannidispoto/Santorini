package it.polimi.ingsw.model;


import it.polimi.ingsw.model.network.action.data.CellInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Battlefield Class represents the board where the game is played
 */
public class Battlefield implements Subject{
    private Cell[][] boardCells;
    public static final int N_ROWS = 5;
    public static final int N_COLUMNS= 5;
    public static final int N_ROWS_VIEW = 5;
    public static final int N_COLUMNS_VIEW= 5;
    private List<Worker> workersInGame;
    private static Battlefield instance = null;
    private List<Observer> observers;

    /**
     * Class constructor
     * */
    private Battlefield() {
        initializeBoard();

    }

    /**
     * Factory method that returns the Battlefield instance (Singleton)
     * @return Battlefield object
     */
    public static Battlefield getBattlefieldInstance(){
        if(instance == null)
            instance = new Battlefield();
        return instance;
    }

    /**
     * Initializes the board with new cells and towers
     */
    private void initializeBoard(){
        boardCells = new Cell[N_COLUMNS][N_ROWS];
        workersInGame = new ArrayList<>();
        observers = new ArrayList<>();
        for(int i = 0; i < N_ROWS; i++)
            for(int j = 0; j < N_COLUMNS; j++)
                boardCells[i][j] = new Cell(new Tower());
    }

    /**
     * Resets the board
     */
    public void cleanField(){
        initializeBoard();
    }

    /**
     * Removes a player's workers from the battlefield
     * @param player whose workers need to be removed
     */
    public void removeWorkers(Player player){
        //removes player's workers from battlefield
        workersInGame.stream()
                        .filter(w->w.getOwnerWorker().equals(player))
                            .forEach(w -> boardCells[w.getRowWorker()][w.getColWorker()].setWorker(null));

        //removes player's workers from workersInGame
        workersInGame = workersInGame.stream()
                        .filter(w->!w.getOwnerWorker().equals(player))
                            .collect(Collectors.toList());
    }

    /**
     * Registers a worker position change
     * @param worker subject of the action
     * @param newColWorker new y coordinate of the worker
     * @param newRowWorker new x coordinate of the worker
     */
    public void updateWorkerPosition(Worker worker, int oldRowWorker, int oldColWorker, int newRowWorker, int newColWorker) throws RuntimeException {
        if(!workersInGame.contains(worker))
            throw new RuntimeException("Trying to update the position of a worker that's not in game");
        boardCells[oldRowWorker][oldColWorker].setWorker(null);
        boardCells[newRowWorker][newColWorker].setWorker(worker);
        notifyUpdate();
    }

    public void updateWorkerPosition(Worker worker,int newRowWorker, int newColWorker) throws RuntimeException {
        if(!workersInGame.contains(worker))
            throw new RuntimeException("Trying to update the position of a worker that's not in game");
        boardCells[newRowWorker][newColWorker].setWorker(worker);
        notifyUpdate();
    }

    public Cell getCell(int x, int y){
        return boardCells[x][y];
    }

    /**
     * Gets the tower inside a cell
     * @param x cell coordinate
     * @param y cell coordinate
     * @return Tower object
     */
   public Tower getTower(int x, int y){
        return boardCells[x][y].getTower();
   }

    /**
     * Generates the worker's view matrix to show the space where are allowed actions (move or build)
     * @param w subject
     * @param p predicate used to check if a particular movement is allowed
     * @return view matrix, a Cell multidimensional array
     */

   public Cell[][] getWorkerView(Worker w, Predicate<Cell> p){
       //gets worker's matrix
        Cell[][] workerView = new Cell[N_ROWS_VIEW][N_COLUMNS_VIEW];

        for(int i = 0; i < N_ROWS_VIEW; i++)
            for (int j = 0; j < N_COLUMNS_VIEW; j++)
                workerView[i][j] = null;

            //workerView[w.getRowWorker()][w.getColWorker()]=boardCells[w.getRowWorker()][w.getColWorker()];

            if((w.getColWorker() - 1 >= 0 && p.test(boardCells[w.getRowWorker()][w.getColWorker() - 1])))
                workerView[w.getRowWorker()][w.getColWorker()-1] = boardCells[w.getRowWorker()][w.getColWorker() - 1];

            if((w.getColWorker() + 1 <= 4 && p.test(boardCells[w.getRowWorker()][w.getColWorker() + 1])))
                workerView[w.getRowWorker()][w.getColWorker() + 1] = boardCells[w.getRowWorker()][w.getColWorker() + 1];

            if((w.getRowWorker() - 1  >= 0 && p.test(boardCells[w.getRowWorker() - 1][w.getColWorker()])))
                workerView[w.getRowWorker()-1][w.getColWorker()] = boardCells[w.getRowWorker() - 1][w.getColWorker()];

            if(w.getRowWorker() + 1 <= 4 && p.test(boardCells[w.getRowWorker() + 1][w.getColWorker()]))
                workerView[w.getRowWorker()+1][w.getColWorker()] = boardCells[w.getRowWorker() + 1][w.getColWorker()];

            if((w.getRowWorker() -1 >= 0 && w.getColWorker() -1 >= 0 && p.test(boardCells[w.getRowWorker()-1][w.getColWorker()-1])))
                workerView[w.getRowWorker()-1][w.getColWorker()-1] = boardCells[w.getRowWorker()-1][w.getColWorker()-1];

            if((w.getRowWorker() -1 >= 0 && w.getColWorker() +1 <= 4 && p.test( boardCells[w.getRowWorker()-1][w.getColWorker()+1]) ))
                workerView[w.getRowWorker()-1][w.getColWorker()+1] =  boardCells[w.getRowWorker()-1][w.getColWorker()+1];

            if((w.getRowWorker() +1 <= 4 && w.getColWorker() -1 >= 0 && p.test( boardCells[w.getRowWorker()+1][w.getColWorker()-1])))
                workerView[w.getRowWorker()+1][w.getColWorker()-1] =  boardCells[w.getRowWorker()+1][w.getColWorker()-1];

            if((w.getRowWorker() +1 <= 4 && w.getColWorker() +1 <= 4 && p.test( boardCells[w.getRowWorker()+1][w.getColWorker()+1])))
                workerView[w.getRowWorker()+1][w.getColWorker()+1] = boardCells[w.getRowWorker()+1][w.getColWorker()+1];

       return workerView;
   }

   public Cell[][] getWorkerView(Worker w){
       return getWorkerView(w, (cell)-> true);
   }

    /**
     *
     * @return the perimeter cell list which contains the list of the perimeter cells
     */
   public List<Cell> getPerimeterCells() {
        List<Cell> perimeterCells = new ArrayList<>();
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        for(int col=0;col<5;col++) {
            perimeterCells.add(battlefield.getCell(0,col));
        }
        for(int col=0;col<5;col++) {
           perimeterCells.add(battlefield.getCell(4,col));
        }
        for(int row=0;row<5;row++){
            perimeterCells.add(battlefield.getCell(row,0));
        }
       for(int row=0;row<5;row++){
           perimeterCells.add(battlefield.getCell(row,4));
       }
        return perimeterCells;
   }

    /**
     * Sets Workers in game
     * @param workersInGameList workers in game
     */
    public void setWorkersInGame(List<Worker> workersInGameList){

        for(Worker w : workersInGameList){
            this.workersInGame.add(w);
        }

       // workersInGame.addAll(workersInGameList);
       //this.workersInGame = new ArrayList<> (workersInGame);
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
        CellInterface[][] battlefield = new CellInterface[N_ROWS][N_COLUMNS];
        String player;
        Color workerColor;

        for(int i = 0; i < N_ROWS; i++){
            for(int j = 0; j < N_COLUMNS; j++){
                player = this.getCell(i,j).getWorker() != null ? this.getCell(i,j).getWorker().getOwnerWorker().getPlayerNickname():null;
                workerColor = player != null ? this.getCell(i,j).getWorker().getOwnerWorker().getPlayerColor(): null;
                battlefield[i][j] = new CellInterface(player,workerColor, this.getCell(i,j).getTower().getHeight(), this.getCell(i,j).getTower().getLastBlock());

            }
        }

        for(Observer o: observers)
            o.update(battlefield, Message.BATTLEFIELD);
    }
}
