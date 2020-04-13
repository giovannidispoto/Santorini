package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.cards.NoLevelUpCondition;

import java.util.ArrayList;

public class NoMoveUp extends MoveEffect {
    /**
     * Class Constructor
     */
    public NoMoveUp() {
        super();
        super.turnStructure = new ArrayList<>();
        turnStructure.add(Step.BUILD);
        turnStructure.add(Step.MOVE);
        turnStructure.add(Step.BUILD);
    }


    /**
     * If you level up with one of your workers, you block the level up of the other players until your next turn
     * @param selectedWorker is the worker selected by the current player
     * @param newRow is the new x coordinate of the worker
     * @param newCol is the new y coordinate of the worker
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Check valid coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol] == null)
            throw new RuntimeException("Unexpected Error!");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();

        //Get previous and next tower height
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;

        //if you went up = true, else = false
        boolean movedWorker = (lvl_a - lvl_b == 1);
        //set value in GlobalEffects
        NoLevelUpCondition.getInstance().setChangeLevel(movedWorker);
        movesLeft--;
    }


}
