package it.polimi.ingsw.model;

/**
 * The Turn class is a generic turn that must be specialized with Card effect
 */
public abstract class Turn {
    private final int N_VIEW_ROWS = 3;
    private final int N_VIEW_COLUMNS = 3;
    protected Match currentMatch;
    private int movesLeft;
    private int blocksLeft;
    private boolean changeLevel;


    /**
     *
     * @param currentMatch
     */
    public Turn(Match currentMatch) {
        this.currentMatch = currentMatch;
    }

    /**
     * End the turn
     */
    public void passTurn(){
        currentMatch.nextPlayer();
    }

    public abstract void checkLocalCondition(Worker selectedWorker);
    public abstract void moveWorker(Worker selectedWorker, int newRow, int newCol);
    public abstract void buildBlock(Worker selectedWorker,int blockRow, int blockCol);
}
