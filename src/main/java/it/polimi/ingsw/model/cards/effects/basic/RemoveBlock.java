package it.polimi.ingsw.model.cards.effects.basic;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Worker;

public class RemoveBlock extends BasicTurn {
    /**
     * Class Constructor
     */
    public RemoveBlock() {
        super();
    }


    public void generateRemoveMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //change selectedWorker in the other worker that has not been moved
        Worker stationaryWorker = selectedWorker.getOwnerWorker().getStationaryWorker(selectedWorker);
        currentMatch.setSelectedWorker(stationaryWorker);
        //setWorkerView without cells where there is a player or a complete tower
        currentMatch.getSelectedWorker().setWorkerView(battlefield.getWorkerView(currentMatch.getSelectedWorker(), (cell)->!cell.isWorkerPresent() && !(cell.getTower().getHeight() == 4)));
        //the user now chooses if to make the build/remove or not and pass the turn (controller)
    }

    public void removeBlock(int delBlockRow,int delBlockCol) {
        //Delete Block
        Battlefield.getBattlefieldInstance().getCell(delBlockRow,delBlockCol).getTower().removeLatestBlock();
        super.passTurn();
    }
}
