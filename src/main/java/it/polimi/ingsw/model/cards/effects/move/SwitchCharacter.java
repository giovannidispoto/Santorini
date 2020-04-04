package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;

public class SwitchCharacter extends MoveEffect {
    /**
     * Class Constructor
     */
    public SwitchCharacter() {
        super();
    }

    /**
     * Allows you to choose a cell occupied by an enemy worker
     * @param selectedWorker is the worker selected by the current player
     */
    @Override
    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
       Cell[][] switchMatrix = battlefield.getWorkerView(selectedWorker, (cell)->!cell.isFriendWorkerPresent(selectedWorker)
                && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
       switchMatrix[selectedWorker.getRowWorker()][selectedWorker.getColWorker()]=Battlefield.getBattlefieldInstance().getCell(selectedWorker.getRowWorker(),selectedWorker.getColWorker());
       return switchMatrix;
    }

    /**
     * Allows you to switch your selected worker with an enemy one besides him during your movement phase
     * @param selectedWorker is the worker selected by the current player
     * @param newRow is the new x coordinate of the worker
     * @param newCol is the new y coordinate of the worker
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol]==null)
            throw new RuntimeException("Unexpected Error!");

        int oldRowSelectedWorker = selectedWorker.getRowWorker();
        int oldColSelectedWorker = selectedWorker.getColWorker();
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        Worker enemyWorker=null;

        if(battlefield.getCell(newRow, newCol).isWorkerPresent())
            enemyWorker = battlefield.getCell(newRow,newCol).getWorker();

        if(enemyWorker==null){
            int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
            selectedWorker.changeWorkerPosition(newRow,newCol);
            int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
            if(lvl_a - lvl_b == 1 && lvl_a == 3)
                reachLevel3 = true;
        }
        else{
            int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
            selectedWorker.changeWorkerPosition(newRow,newCol);
            int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
            if(lvl_a - lvl_b == 1 && lvl_a == 3)
                reachLevel3 = true;
            battlefield.getCell(oldRowSelectedWorker,oldColSelectedWorker).setWorker(enemyWorker);
        }
        movesLeft--;
    }
}
