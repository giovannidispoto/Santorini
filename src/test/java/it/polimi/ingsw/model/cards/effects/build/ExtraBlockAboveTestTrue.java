package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.Alphanumeric.class)
class ExtraBlockAboveTestTrue {

    final Player p1 = new Player("Steve Wozniak", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);

    @Test
    void extraAboveTrue() {
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

        //Simulation : CURRENT PLAYER - Steve Wozniak
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,2);
        //3. Build() again
        t.buildBlock(m.getSelectedWorker(),2,2);

        //ASSERTS
        assertTrue(battlefield.getCell(2,2).getTower().getHeight()==2);
    }

    @Test
    void extraAboveTrueException() {
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

        //Simulation : CURRENT PLAYER - Steve Wozniak
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,2);

        //ASSERTS
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(m.getSelectedWorker(),2,1));
        assertEquals("Unexpected Error!", expectedException.getMessage());
    }
}