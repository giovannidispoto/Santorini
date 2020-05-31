package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * NoLevelUpCondition Class describes a global effect
 */
public class NoLevelUpCondition extends GlobalEffect {

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
        if(changeLevel) {
            //deny only level up
            //TODO: Modify condition
            return battlefield.getWorkerView(w, (cell) -> battlefield.getCell(w.getRowWorker(), w.getColWorker()).getTower().getHeight() >= cell.getTower().getHeight());
        }
        //in case applyEffect is called even if changeLevel==false
        else{
            //don't deny level up, so don't change WorkerView
            return w.getWorkerView();
        }
    }
}
