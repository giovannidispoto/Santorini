package it.polimi.ingsw.model;

/**
 * Turn class represents a generic turn that must be specialized with a card effect
 */
public abstract class Turn {

    private final int N_VIEW_ROWS = 5;
    private final int N_VIEW_COLUMNS = 5;
    protected Match currentMatch;
    protected int movesLeft;
    protected int blocksLeft;
    protected int moves;
    protected int blocks;
    private boolean changeLevel;
    protected boolean reachedLevel3 = false;

    /**
     * Class Constructor
    *
     public Turn(Match currentMatch) {
        this.currentMatch = currentMatch;
    }
    */


    public Turn(){
        //null
    }

    /**
     *
     * @param currentMatch
     */
    public void setCurrentMatch(Match currentMatch){
        this.currentMatch = currentMatch;
    }

    /**
     * Manages what happens at the end of the turn
     */
    public void passTurn(){
        currentMatch.nextPlayer();
    }
    /**
     * Get moves left in the turn
     * @return moves left
     */
    public int getMovesLeft(){
        return movesLeft;
    }

    /**
     * Get blocks left in the turn
     * @return blocks left
     */
    public int getBlocksLeft(){
        return blocksLeft;
    }
    /**
     * Abstract methods that are specialized by the sons of this class
     */
    public abstract void checkLocalCondition(Worker selectedWorker);
    public abstract void moveWorker(Worker selectedWorker, int newRow, int newCol);
    public abstract void buildBlock(Worker selectedWorker,int blockRow, int blockCol);

    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent() && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
        /*Cell[][] returnMatrix = battlefield.getWorkerView(selectedWorker, (cell)->battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
        returnMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=null;
        return returnMatrix;*/
    };
    public Cell[][] generateBuildingMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        /*Cell[][] returnMatrix = battlefield.getWorkerView(selectedWorker);
        returnMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=null;
        return returnMatrix;*/
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent());
    };

    public void resetTurn(){
        this.movesLeft = this.moves;
        this.blocksLeft = this.blocks;
    }
}
