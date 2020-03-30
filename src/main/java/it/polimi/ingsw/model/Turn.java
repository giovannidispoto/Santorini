package it.polimi.ingsw.model;

/**
 * Turn class represents a generic turn that must be specialized with a card effect
 */
public abstract class Turn {

    private final int N_VIEW_ROWS = 3;
    private final int N_VIEW_COLUMNS = 3;
    protected Match currentMatch;
    private int movesLeft;
    private int blocksLeft;
    private boolean changeLevel;


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
     * Abstract methods that are specialized by the sons of this class
     */
    public abstract void checkLocalCondition(Worker selectedWorker);
    public abstract void moveWorker(Worker selectedWorker, int newRow, int newCol);
    public abstract void buildBlock(Worker selectedWorker,int blockRow, int blockCol);
}
