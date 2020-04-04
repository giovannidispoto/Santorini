package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.cards.effects.build.BuildEffect;

public class ExtraBlockAbove extends BuildEffect{
    private boolean blockAbove;
    /**
     * Class Constructor
     */
    public ExtraBlockAbove(boolean blockAbove,int blocksLeft) {
        super();
        this.blockAbove=blockAbove;
        this.blocksLeft=blocksLeft;
    }

    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Hephaestus
        if(blockAbove){
            if(blocksLeft>0){
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->cell.equals(battlefield.getCell(newBlockRow,newBlockCol))));
            }
            else{
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            }
            blocksLeft--;
        }
        //Demeter
        else{
            if(blocksLeft>0){
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
                selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker, (cell)->!cell.isWorkerPresent()
                        && !cell.equals(battlefield.getCell(newBlockRow,newBlockCol))));
            }
            else{
                Battlefield.getBattlefieldInstance().getTower(newBlockRow,newBlockCol).addNextBlock();
            }
            blocksLeft--;
        }
    }
}
