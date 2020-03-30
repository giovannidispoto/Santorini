package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * NoLevelUpCondition Class describes a global effect
 */
public class NoLevelUpCondition extends GlobalEffect {

    private boolean changeLevel;
    private NoLevelUpCondition instance = null;


    public GlobalEffect getInstance(){
        if(instance == null)
            instance = new NoLevelUpCondition();
        return instance;
    }

    public void setChangeLevel(boolean changeLevel){
        this.changeLevel = changeLevel;
    }

    private NoLevelUpCondition(){
        this.changeLevel = false;
    }

    @Override
    public Cell[][] applyEffect(Worker w) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        return battlefield.getWorkerView(w, (cell)->cell.getTower().getHeight()<= battlefield.getCell(w.getRowWorker(),w.getColWorker()).getTower().getHeight());
    }
}
