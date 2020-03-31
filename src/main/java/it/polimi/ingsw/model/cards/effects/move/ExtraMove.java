package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

/**
 * ExtraMove class represent the effect that allow player more movement
 */
public class ExtraMove extends MoveEffect {
    /**
     * @param currentMatch
     * @param movesLeft
     */
    public ExtraMove(Match currentMatch, int movesLeft) {
        super(currentMatch);
        this.movesLeft = movesLeft;
    }

    /**
     *
     * @param selectedWorker
     * @param newRow
     * @param newCol
     * @throws RuntimeException
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) throws RuntimeException {
        if(selectedWorker.getWorkerView()[newRow][newCol] == null)
            throw new RuntimeException("Illegal coordinates for worker");

        int oldRow = selectedWorker.getRowWorker();
        int oldCol = selectedWorker.getColWorker();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        movesLeft--;
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        if(movesLeft > 0) {
            Cell[][] newView = battlefield.getWorkerViewForMove(selectedWorker);
            newView[oldRow][oldCol] = null;
            selectedWorker.setWorkerView(newView);
        }
        //Set Worker Build Matrix
        if(movesLeft==0)
            selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker,(cell)->!cell.isWorkerPresent()));

    }


}
