package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DomeEverywhereTest {
    final Player p1 = new Player("Chester Bennington", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);

    @Test
    void groundDome() {
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

        //Simulation : CURRENT PLAYER - Chester Bennington
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,1);

        //ASSERT : We expect a DOME above the ground level
        assertTrue(battlefield.getCell(2,1).getTower().getLastBlock().equals(Block.DOME));

        battlefield.cleanField();
    }

    @Test
    void extraDomeException() {
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

        //Simulation : CURRENT PLAYER - Chester Bennington
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));

        //ASSERTS
        //Building under the current worker
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(m.getSelectedWorker(),1,1));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }
}