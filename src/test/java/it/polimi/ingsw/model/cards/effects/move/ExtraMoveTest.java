package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.Alphanumeric.class)
class ExtraMoveTest {

    final Player p1 = new Player("Mark Zuckerberg", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);

    @Test
    void tripleMove() {
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

        //Simulation : CURRENT PLAYER - Mark Zuckerberg
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),2,2);
        //4. Move () again
        t.moveWorker(m.getSelectedWorker(),3,3);
        //5. Move () again and again
        t.moveWorker(m.getSelectedWorker(),4,4);
        //ASSERT : We expect that the worker has reached the cell[3][3]
        assertTrue(battlefield.getCell(4,4).getWorker().equals(w1));
    }

    @Test
    void tryToComeBack() {
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

        //Simulation : CURRENT PLAYER - Mark Zuckerberg
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),2,2);
        //4. Move () again
        t.moveWorker(m.getSelectedWorker(),3,3);

        //ASSERTS
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.moveWorker(m.getSelectedWorker(),2,2));
        assertEquals("Unexpected Error!", expectedException.getMessage());
    }
}