package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Worker;

import java.util.ArrayList;

public class ExtraMovePerimeter extends MoveEffect {
    /**
     * Class Constructor
     */
    public ExtraMovePerimeter() {
        super();
        super.turnStructure = new ArrayList<>();
        super.turnStructure.add(Step.MOVE);
        super.turnStructure.add(Step.BUILD);
        super.turnStructure.add(Step.END);
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
        //basic move
        selectedWorker.changeWorkerPosition(newRow,newCol);
        //check whether to apply the effect
        if(!battlefield.getPerimeterCells().contains(battlefield.getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker()))){
            //If the new cell is not a perimetral cell
            movesLeft--;    //set moves to 0
        }
        else{
            //Otherwise... just update the movement matrix, ready for a new basic move
            selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                    && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight()
                    && !(cell.getTower().getLastBlock() == Block.DOME)));
        }
        //check win
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;
    }


}