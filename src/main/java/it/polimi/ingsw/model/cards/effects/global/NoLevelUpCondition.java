package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * NoLevelUpCondition Class describes a global effect
 */
public class NoLevelUpCondition extends GlobalEffect {
    private static final String associatedEffect = "NoMoveUp";
    private boolean changeLevel;

    /**
     * Class constructor
     * */
    private NoLevelUpCondition(){
        this.changeLevel = false;
    }

    private static final ThreadLocal<NoLevelUpCondition> NO_LEVEL_UP_CONDITION_THREAD_LOCAL = ThreadLocal.withInitial(NoLevelUpCondition::new);

    /**
     * Factory method that returns the NoLevelUpCondition instance (Singleton per Thread)
     * @return NoLevelUpCondition object
     */
    public static NoLevelUpCondition getInstance(){
        return NO_LEVEL_UP_CONDITION_THREAD_LOCAL.get();
    }

    /**
     * Informs NoLevelUpCondition, what kind of move was made,
     * in particular it informs through the parameter if the worker has made a level up
     * @param changeLevel   true, if the worker has leveled up, false if he has not leveled up
     */
    public void setChangeLevel(boolean changeLevel){
        this.changeLevel = changeLevel;
    }

    /**
     * Restore Effect when a new match is created
     */
    public void restoreEffect(){
        this.changeLevel = false;
    }

    @Override
    public Cell[][] applyEffect(Worker w) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //If the effect is active, we check that the effect is not called on the card associated with the effect
        if(changeLevel && !w.getOwnerWorker().getPlayerCard().getCardEffect().equalsIgnoreCase(associatedEffect)) {
            Cell [][] workerView = w.getWorkerView();
            int currentWorkerHeight = battlefield.getCell(w.getRowWorker(), w.getColWorker()).getTower().getHeight();
            //Deny only level up
            for(int row=0; row < Battlefield.N_ROWS; row++){
                for(int col=0; col < Battlefield.N_COLUMNS; col++){
                    /*Scroll the already created workerView, we will only modify it, checking only the non-null cells,
                      if the height of the tower is greater than the current where the worker is, it will be made null (the worker cannot go up)
                      if the height is equal or lower we do not change anything, it is assumed that it has already been correctly generated
                     */
                    if(null != workerView[row][col] && workerView[row][col].getTower().getHeight() > currentWorkerHeight){
                        workerView[row][col] = null;
                    }
                }
            }
            return workerView;
        }
        //in case applyEffect is called even if changeLevel==false
        else{
            //don't deny level up, so don't change WorkerView
            return w.getWorkerView();
        }
    }
}
