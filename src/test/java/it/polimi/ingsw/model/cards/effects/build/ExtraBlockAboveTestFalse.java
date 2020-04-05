package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtraBlockAboveTestFalse {

    final Player p1 = new Player("Steve Wozniak", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);
    //Testing extra block above set to false : DEMETER
    @Test
    void extraAboveFalse() {
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
        t.buildBlock(m.getSelectedWorker(),2,1);
        //3. Build() again
        t.buildBlock(m.getSelectedWorker(),0,1);

        //ASSERTS
        assertTrue(battlefield.getCell(2,1).getTower().getHeight()==1);
        assertTrue(battlefield.getCell(0,1).getTower().getHeight()==1);

        battlefield.cleanField();
    }

    @Test
    void extraAboveFalseException() {
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
        t.buildBlock(m.getSelectedWorker(),2,1);

        //ASSERTS
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(m.getSelectedWorker(),2,1));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }

}