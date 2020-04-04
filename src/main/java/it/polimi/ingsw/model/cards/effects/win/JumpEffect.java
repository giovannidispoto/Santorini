package it.polimi.ingsw.model.cards.effects.win;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Worker;

public class JumpEffect extends WinEffect {
    private int oldRow;
    private int oldCol;

    /**
     * Class Constructor
     */
    public JumpEffect() {
        super();
    }

    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Save old coordinates
        oldRow=selectedWorker.getRowWorker();
        oldCol=selectedWorker.getColWorker();
        //Make a move
        int lvl_b = Battlefield.getBattlefieldInstance().getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = Battlefield.getBattlefieldInstance().getCell(newRow, newCol).getTower().getHeight();
        //Check if we have reached the level 3
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;
    }

    @Override
    public void checkLocalCondition(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Check if we have made a two level jump
        if(battlefield.getCell(oldRow,oldCol).getTower().getHeight()-battlefield.getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker()).getTower().getHeight()==2){
            //currentMatch.declareWinner(); that return the match winner... we need the controller
        }
        //Check if we have reached the level 3
        else if(reachedLevel3){
            //currentMatch.declareWinner(); that returns the match winner... we need the controller
        }
    }
}
