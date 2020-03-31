package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.cards.effects.build.BuildEffect;

public class BlockUnder extends MoveEffect {
    /**
     * @param currentMatch
     */
    public BlockUnder(Match currentMatch) {
        super(currentMatch);
    }

    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) throws RuntimeException {
        if(selectedWorker.getWorkerView()[newRow][newCol] == null)
            throw new RuntimeException("Illegal coordinates for worker");

        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        selectedWorker.changeWorkerPosition(newRow,newCol);
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachLevel3 = true;

        //Includes the cell where there is the worker, normally excluded from the build matrix
        selectedWorker.setWorkerView(battlefield.getWorkerView(selectedWorker,(cell)->!cell.isWorkerPresent()
                || cell.equals(battlefield.getCell(newRow,newCol))));
    }
}
