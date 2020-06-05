package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.controller.Step;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;
import java.util.List;

public class ExtraMovePerimeter extends MoveEffect {
    /**
     * Class Constructor
     */
    public ExtraMovePerimeter(List<Step> turnStructure) {
        super.turnStructure = new ArrayList<>(turnStructure);
    }


    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol] == null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //check towers levels
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        //Regular Move
        selectedWorker.changeWorkerPosition(newRow,newCol);
        //check whether to apply the effect
        if(!battlefield.getPerimeterCells().contains(battlefield.getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker()))){
            //If the new cell is not a perimeter cell
            movesLeft--;    //set moves to 0
        }
        else{
            //Otherwise... just update the movement matrix, ready for a new Regular move
            selectedWorker.setWorkerView(super.generateMovementMatrix(selectedWorker));
        }
        //check win
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;
    }

    @Override
    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        //Regular Move
        return super.generateMovementMatrix(selectedWorker);
    }


}