package it.polimi.ingsw.model;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The Battlefield class represent the board where game is played
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
     * Factory method that return the battelfield istance that is a singleton
     * @return Battlefield
     */
    public static Battlefield getBattelfieldInstance(){
        if(instance == null)
            instance = new Battlefield();
        return instance;
    }

    /**
     * Set workers in game
     * @param workersInGame workers in game
     */
    public void setWorkersInGame(List<Worker> workersInGame){
        this.workersInGame = List.copyOf(workersInGame);
    }
    /**
     * Create battelfield
     * */
    private Battlefield() {
        initializeBoard();
    }

    /**
     * Inizalize the board with new Cell and Tower
     */
    private void initializeBoard(){
        boardCells = new Cell[N_COLUMNS][N_ROWS];
        for(int i = 0; i < N_ROWS; i++)
            for(int j = 0; j < N_COLUMNS; j++)
                boardCells[i][j] = new Cell(new Tower());
    }

    /**
     * Reset actual state of Battelfield
     */
    public void cleanField(){
        initializeBoard();
        workersInGame = null;
    }

    /**
     * Remove worker of a Player
     * @param player Player to remove from the game
     */
    public void removeWorker(Player player){
        workersInGame = workersInGame.stream()
                        .filter(w->!w.getOwnerWorker().equals(player))
                            .collect(Collectors.toList());
    }

    /**
     *
     * @param worker
     * @param newColWorker
     * @param newRowWorker
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
     *
     * @param x
     * @param y
     * @return
     */
   public Tower getTower(int x, int y){
        return boardCells[x][y].getTower();
   }

    /**
     * Calculate the view matrix used from the worker to know how to move
     * @param w worker
     * @param p predicate used for checking if some movement are permitted
     * @return view matrix
     */
   public Cell[][] getWorkerView(Worker w, Predicate<Cell> p){
       //get worker matrix
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
}
