package it.polimi.ingsw.model.cards.effetcs.basic;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.Worker;

public class BasicTurn extends Turn {

    private boolean reachLevel3 = false;

    /**
     * @param currentMatch
     */
    public BasicTurn(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        int lvl_b = Battlefield.getBattelfieldInstance().getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = Battlefield.getBattelfieldInstance().getCell(newRow, newCol).getTower().getHeight();
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachLevel3 = true;
    }

    @Override
    public void buildBlock(Worker selectedWorker, int x, int y) {
        int lvOne = Battlefield.getBattelfieldInstance().getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
       Battlefield.getBattelfieldInstance().getTower(x,y).addNextBlock();

       if(Battlefield.getBattelfieldInstance().getTower(x,y).getHeight() == 3 && lvOne == 2)
           reachLevel3 = true;
    }

    public void checkLocalCondition(Worker selectedWorker){
        //controllo le condizioni di vittoria per Pan
        if(reachLevel3)
                currentMatch.reachLevel3();
    }


}
