package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Worker;

public class ExtraBlockAbove extends BuildEffect{
    private boolean buildInSameCell;
    /**
     * Class Constructor
     * @param buildInSameCell boolean to split the effects
     * @param blocksLeft set blocksLeft in turn from file
     */
    public ExtraBlockAbove(boolean buildInSameCell,int blocksLeft) {
        super();
        this.buildInSameCell=buildInSameCell;
        this.blocksLeft=blocksLeft;
        this.blocks=blocksLeft;
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
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->cell.equals(battlefield.getCell(newBlockRow,newBlockCol))
                && !(cell.getTower().getHeight() > 2)));
            }
            else if(blocksLeft == 1){
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            }
            blocksLeft--;
        }
        //Demeter
        else{
            if(blocksLeft > 1){
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                //Remove in th WorkingMatrix the cell where you've build now
                selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                        && !cell.equals(battlefield.getCell(newBlockRow,newBlockCol))));
            }
            else if(blocksLeft == 1){
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            }
            blocksLeft--;
        }
    }
}
