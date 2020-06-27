package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.controller.Step;

import java.util.ArrayList;
import java.util.List;

public class SwitchCharacter extends MoveEffect {
    /**
     * Class Constructor
     * @param turnStructure structure of the turn
     */
    public SwitchCharacter(List<Step> turnStructure) {
        super();
        super.turnStructure = new ArrayList<>(turnStructure);
    }


    /**
     * Allows you to choose a cell occupied by an enemy worker
     * @param selectedWorker is the worker selected by the current player
     */
    @Override
    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //Deny move in this cells: your workers, higher than one than the worker, Domes
        //Allow: enemy workers cells
        return battlefield.getWorkerView(selectedWorker, (cell)->!cell.isFriendWorkerPresent(selectedWorker)
                && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight()
                && !(cell.getTower().getLastBlock() == Block.DOME));
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

        //check towers levels
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        //basic move
        selectedWorker.changeWorkerPosition(newRow,newCol);
        //check whether to apply switch effect
        if (enemyWorker != null) {
            enemyWorker.setWorkerPosition(oldRowSelectedWorker,oldColSelectedWorker);
        }
        //check win
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;

        movesLeft--;
    }
}