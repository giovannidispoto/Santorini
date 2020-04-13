package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.effects.build.ExtraBlockPerimetral;

import java.util.ArrayList;
import java.util.List;

public class ExtraMovePerimeter extends MoveEffect {
    /**
     * Class Constructor
     */
    public ExtraMovePerimeter() {
        super();
        super.turnStructure = new ArrayList<>();
        turnStructure.add(Step.MOVE);
        turnStructure.add(Step.BUILD);
    }


    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        if(!battlefield.getPerimeterCells().contains(battlefield.getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker()))){
            //If the new cell is not a perimetral cell
            movesLeft--;
        }
        else{
            //Otherwise... just update the movement matrix, ready for a new move
            selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent() && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight()));
        }
    }


}
