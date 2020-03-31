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
    private boolean changeLevel;
    protected boolean reachLevel3 = false;

    /**
     * Class Constructor
     * @param currentMatch that holds all the parameters of the current match
     */
    public Turn(Match currentMatch) {
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
}
