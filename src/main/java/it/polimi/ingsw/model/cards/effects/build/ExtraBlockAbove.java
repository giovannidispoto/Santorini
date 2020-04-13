package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class ExtraBlockAbove extends BuildEffect{
    private boolean buildInSameCell;
    /**
     * Class Constructor
     * @param buildInSameCell boolean to split the effects
     * @param blocksLeft set blocksLeft in turn
     */
    public ExtraBlockAbove(boolean buildInSameCell,int blocksLeft) {
        super();
        this.buildInSameCell=buildInSameCell;
        this.blocksLeft=blocksLeft;
        this.blocks=blocksLeft;
        super.turnStructure = new ArrayList<>();
        turnStructure.add(Step.MOVE);
        turnStructure.add(Step.BUILD);
    }


    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Hephaestus
        if(buildInSameCell){
            if(blocksLeft > 1){
                //Regular Build
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                //Set WorkerView for Special Build (Same cell where has built now, but can't build a dome, so no towerLv3)
                selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->cell.equals(battlefield.getCell(newBlockRow,newBlockCol))
                        && !(cell.getTower().getHeight() > 2)
                        && !(cell.getTower().getLastBlock() == Block.DOME)));
            }
            else if(blocksLeft == 1){
                //Special Build
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            }
        }
        //Demeter
        else{
            if(blocksLeft > 1){
                //Regular Build
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                //Remove in the WorkerMatrix the cell where you've build now, Banned Cells with: dome, worker
                selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                        && !cell.equals(battlefield.getCell(newBlockRow,newBlockCol))
                        && !(cell.getTower().getLastBlock() == Block.DOME)));
            }
            else if(blocksLeft == 1){
                //Special Build
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            }
        }

        blocksLeft--;
    }


}
