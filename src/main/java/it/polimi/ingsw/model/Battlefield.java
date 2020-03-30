package it.polimi.ingsw.model;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Battlefield Class represents the board where the game is played
 */
public class Battlefield {
    private Cell[][] boardCells;
    private static int N_ROWS = 5;
    private static int N_COLUMNS= 5;
    private static int N_ROWS_VIEW = 3;
    private static int N_COLUMNS_VIEW= 3;
    private List<Worker> workersInGame;
    private static Battlefield instance = null;

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
        for(int i = 0; i < N_ROWS; i++)
            for(int j = 0; j < N_COLUMNS; j++)
                boardCells[i][j] = new Cell(new Tower());
    }

    /**
     * Resets the board
     */
    public void cleanField(){
        initializeBoard();
        workersInGame = null;
    }

    /**
     * Removes a player's worker
     * @param player who's worker has to be removed
     */
    public void removeWorker(Player player){
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
    }

    public void updateWorkerPosition(Worker worker,int newRowWorker, int newColWorker) throws RuntimeException {
        if(!workersInGame.contains(worker))
            throw new RuntimeException("Trying to update the position of a worker that's not in game");
        boardCells[newRowWorker][newColWorker].setWorker(worker);
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

        //workerView[1][1] = boardCells[w.getRowWorker()][w.getColWorker()];
        workerView[1][1] = null;
        workerView[1][0] = (w.getColWorker() - 1 >= 0 && p.test(boardCells[w.getRowWorker()][w.getColWorker() - 1])) ? boardCells[w.getRowWorker()][w.getColWorker() - 1] : null ;
        workerView[1][2] = (w.getColWorker() + 1 <= 4 && p.test(boardCells[w.getRowWorker()][w.getColWorker() + 1])) ? boardCells[w.getRowWorker()][w.getColWorker() + 1] : null ;
        workerView[0][1] = (w.getRowWorker() - 1  >= 0 && p.test(boardCells[w.getRowWorker() - 1][w.getColWorker()])) ? boardCells[w.getRowWorker()-1][w.getColWorker()] : null ;
        workerView[2][1] = (w.getRowWorker() + 1 <= 4 && p.test(boardCells[w.getRowWorker() + 1][w.getColWorker()])) ? boardCells[w.getRowWorker()+1][w.getColWorker()] : null ;
        workerView[0][0] = (w.getRowWorker() -1 >= 0 && w.getColWorker() -1 >= 0 && p.test(boardCells[w.getRowWorker()-1][w.getColWorker()-1]) ) ? boardCells[w.getRowWorker()-1][w.getColWorker()-1] : null ;
        workerView[0][2] = (w.getRowWorker() -1 >= 0 && w.getColWorker() +1 <= 4 && p.test( boardCells[w.getRowWorker()-1][w.getColWorker()+1]) ) ? boardCells[w.getRowWorker()-1][w.getColWorker()+1] : null ;
        workerView[2][0] = (w.getRowWorker() +1 <= 4 && w.getColWorker() -1 >= 0 && p.test( boardCells[w.getRowWorker()+1][w.getColWorker()-1])) ? boardCells[w.getRowWorker()+1][w.getColWorker()-1] : null ;
        workerView[2][2] = (w.getRowWorker() +1 <= 4 && w.getColWorker() +1 <= 4 && p.test( boardCells[w.getRowWorker()+1][w.getColWorker()+1]) ) ? boardCells[w.getRowWorker()+1][w.getColWorker()+1] : null ;

       return workerView;
   }

   public Cell[][] getWorkerView(Worker w){
       return getWorkerView(w, (cell)-> true);
   }

    /**
     * Sets Workers in game
     * @param workersInGame workers in game
     */

    public void setWorkersInGame(List<Worker> workersInGame){
        this.workersInGame = List.copyOf(workersInGame);
    }
}
