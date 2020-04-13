package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class ExtraBlockPerimetral extends BuildEffect {
    /**
     * Class Constructor
     * @param blocksLeft set blocksLeft and blocks in turn
     */
    public ExtraBlockPerimetral(int blocksLeft) {
        super();
        this.blocksLeft = blocksLeft;
        this.blocks = blocksLeft;
        super.turnStructure = new ArrayList<>();
        turnStructure.add(Step.MOVE);
        turnStructure.add(Step.BUILD);
    }

    /**
     * Allows you to make an extra special build after a first (or more) regular build
     * (special build: you can't build in perimetersCells + basic build rules)
     * @param selectedWorker is the worker selected by the current player
     * @param newBlockRow is the x coordinate of the new block
     * @param newBlockCol is the y coordinate of the new block
     */
    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        if(blocksLeft > 1){
            //Regular Build
            Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            //Set WorkerView for Special Build (No Perimeter Cells, No Workers Cells, No Domes)
            selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                    && !battlefield.getPerimeterCells().contains(cell)
                    && !(cell.getTower().getLastBlock() == Block.DOME)));
        }
        else if(blocksLeft == 1){
            //Special Build (can't build in perimeterCells, in workers cells and on domes)
            Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
        }

        blocksLeft--;
    }


}
