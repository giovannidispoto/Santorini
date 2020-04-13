package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;


/**
 * ExtraMove class represents the effect that allows the player to do an extra move
 */
public class ExtraMove extends MoveEffect {
    /**
     * Class Constructor
     */
    public ExtraMove(int movesLeft){
        super();
        this.movesLeft = movesLeft;
        this.moves = movesLeft;
        super.turnStructure = new ArrayList<>();
        turnStructure.add(Step.MOVE);
        turnStructure.add(Step.BUILD);
    }


    /**
     * This method allows you to do an extra move (but not in the old cell)
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     * @param newRow is the x coordinate of the destination cell
     * @param newCol is the y coordinate of the destination cell
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) throws RuntimeException {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Save actual position
        int oldRow = selectedWorker.getRowWorker();
        int oldCol = selectedWorker.getColWorker();
        if(movesLeft>0){
            int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
            selectedWorker.changeWorkerPosition(newRow,newCol);
            int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
            if(lvl_a - lvl_b == 1 && lvl_a == 3)
                reachedLevel3 = true;
            //Cell[][] updatedMatrix = generateMovementMatrix(selectedWorker);
            //updatedMatrix[oldRow][oldCol]=null;
            //selectedWorker.setWorkerView(updatedMatrix);
            selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent() && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight() && !cell.equals(battlefield.getCell(oldRow,oldCol))));
        }
        else{
            int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
            selectedWorker.changeWorkerPosition(newRow,newCol);
            int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
            if(lvl_a - lvl_b == 1 && lvl_a == 3)
                reachedLevel3 = true;
        }
        movesLeft--;
    }
}
