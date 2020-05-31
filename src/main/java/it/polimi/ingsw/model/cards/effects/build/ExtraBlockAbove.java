package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Step;

import java.util.ArrayList;
import java.util.List;

public class ExtraBlockAbove extends BuildEffect{
    private final boolean buildInSameCell;
    private int lastBuiltBlockRow_SameCell;
    private int lastBuiltBlockCol_SameCell;
    private int lastBuiltBlockRow_NotSameCell;
    private int lastBuiltBlockCol_NotSameCell;

    /**
     * Class Constructor
     * @param buildInSameCell boolean to split the effects
     * @param blocksLeft set blocksLeft in turn
     */
    public ExtraBlockAbove(boolean buildInSameCell,int blocksLeft, List<Step> turnStructure) {
        super();
        this.buildInSameCell=buildInSameCell;
        this.blocksLeft=blocksLeft;
        this.blocks=blocksLeft;
        super.turnStructure = new ArrayList<>(turnStructure);
    }


    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //HEPHAESTUS
        if(buildInSameCell){
            if(blocksLeft > 1){
                //Regular Build
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                this.lastBuiltBlockRow_SameCell = newBlockRow;
                this.lastBuiltBlockCol_SameCell = newBlockCol;
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
        //DEMETER
        else{
            if(blocksLeft > 1){
                //Regular Build
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                this.lastBuiltBlockRow_NotSameCell = newBlockRow;
                this.lastBuiltBlockCol_NotSameCell = newBlockCol;
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

    @Override
    public Cell[][] generateBuildingMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Special Build
        if(blocksLeft == 1){
            //HEPHAESTUS
            if(buildInSameCell){
                //Set WorkerView for Special Build (Same cell where has built now, but can't build a dome, so no towerLv3)
                return battlefield.getWorkerView(selectedWorker, (cell)->cell.equals(battlefield.getCell(this.lastBuiltBlockRow_SameCell,this.lastBuiltBlockCol_SameCell))
                        && !(cell.getTower().getHeight() > 2)
                        && !(cell.getTower().getLastBlock() == Block.DOME));
            }
            //DEMETER
            else{
                //Remove in the WorkerMatrix the cell where you've build now, Banned Cells with: dome, worker
                return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                        && !cell.equals(battlefield.getCell(this.lastBuiltBlockRow_NotSameCell,this.lastBuiltBlockCol_NotSameCell))
                        && !(cell.getTower().getLastBlock() == Block.DOME));
            }
        }
        //Regular Build
        return super.generateBuildingMatrix(selectedWorker);
    }


}
