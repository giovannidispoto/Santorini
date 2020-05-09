package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * NoLevelUpCondition Class describes a global effect
 */
public class NoLevelUpCondition extends GlobalEffect {

    private boolean changeLevel;
    private static NoLevelUpCondition instance = null;


    public static NoLevelUpCondition getInstance(){
        if(instance == null)
            instance = new NoLevelUpCondition();
        return instance;
    }

    /**
     *
     * @param changeLevel
     */
    public void setChangeLevel(boolean changeLevel){
        this.changeLevel = changeLevel;
    }

    private NoLevelUpCondition(){
        this.changeLevel = false;
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
