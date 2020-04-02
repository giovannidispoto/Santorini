package it.polimi.ingsw.model.cards.effects.basic;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockUnderTest {

    final  Player p1 = new Player("Steve Jobs", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);

    @Test
    void buildUnderYourself() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Steve Jobs
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),m.getSelectedWorker().getRowWorker(),m.getSelectedWorker().getColWorker());

        //ASSERT : We expect a new block under the selected worker
        assertTrue(battlefield.getCell(m.getSelectedWorker().getRowWorker(),m.getSelectedWorker().getColWorker()).getTower().getHeight()==1);
    }
}