package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.server.Step;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;
import java.util.List;


/**
 * ExtraMove class represents the effect that allows the player to do an extra move
 */
public class ExtraMove extends MoveEffect {
    int oldRow;
    int oldCol;

    /**
     * Class Constructor
     */
    public ExtraMove(int movesLeft, List<Step> turnStructure){
        super();
        this.movesLeft = movesLeft;
        this.moves = movesLeft;
        super.turnStructure = new ArrayList<>(turnStructure);
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
        this.oldRow = selectedWorker.getRowWorker();
        this.oldCol = selectedWorker.getColWorker();
        //check towers levels
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();

        if(movesLeft > 1){
            //Regular Move
            selectedWorker.changeWorkerPosition(newRow,newCol);
            //Set WorkerView for Special Move (No previous cell, ,No taller than 1 of the worker, No Workers Cells, No Domes)
            selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                    && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight()
                    && !cell.equals(battlefield.getCell(this.oldRow, this.oldCol))
                    && !(cell.getTower().getLastBlock() == Block.DOME)));
        }
        else if (movesLeft == 1){
            //Special Move
            selectedWorker.changeWorkerPosition(newRow,newCol);
        }
        //set win
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;

        movesLeft--;
    }

    @Override
    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Special Move
        if(movesLeft == 1){
            //Set WorkerView for Special Move (No previous cell, ,No taller than 1 of the worker, No Workers Cells, No Domes)
            return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                    && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight()
                    && !cell.equals(battlefield.getCell(this.oldRow, this.oldCol))
                    && !(cell.getTower().getLastBlock() == Block.DOME));
        }
        //Regular Move
        return super.generateMovementMatrix(selectedWorker);
    }
}