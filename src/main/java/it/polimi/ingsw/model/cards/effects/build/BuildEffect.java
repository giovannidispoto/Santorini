package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

/**
 * The BuildEffect card represent class of cards that have a build effect, assuming that the move is basic according to rules of the game
 */
public abstract class BuildEffect extends Turn {

    /**
     * @param currentMatch
     */
   // public BuildEffect(Match currentMatch) {
   //     super(currentMatch);
   // }

    public BuildEffect(){
        //null
    }

    public void moveWorker(Worker selectedWorker, int newRow, int newCol){
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachLevel3 = true;
        //Set Worker Build Matrix
        selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker,(cell)->!cell.isWorkerPresent()));
    }


    public void checkLocalCondition(Worker currentWorker){
        if(reachLevel3)
            currentMatch.reachLevel3();
    }



}
