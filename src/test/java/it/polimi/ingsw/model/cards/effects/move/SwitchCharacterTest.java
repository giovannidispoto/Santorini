package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SwitchCharacterTest {

    final Player p1 = new Player("Ferruccio Resta", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Franco Anelli", LocalDate.now(), Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);

    @Test
    void switchMovement() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        w2.setWorkerPosition(1,0);
        w3.setWorkerPosition(1,2);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Ferruccio Resta
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),1,2);

        //ASSERT : We expect that the worker has reached the cell[3][3]
        assertTrue(battlefield.getCell(1,2).getWorker().equals(w1));
        assertTrue(battlefield.getCell(1,1).getWorker().equals(w3));
        assertTrue(battlefield.getCell(1,0).getWorker().equals(w2));
    }

    @Test
    void normalMovement() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        w2.setWorkerPosition(1,0);
        w3.setWorkerPosition(1,2);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Ferruccio Resta
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),0,1);

        //ASSERT : We expect that the worker has reached the cell[3][3]
        assertTrue(battlefield.getCell(0,1).getWorker().equals(w1));
        assertTrue(battlefield.getCell(1,2).getWorker().equals(w3));
        assertTrue(battlefield.getCell(1,0).getWorker().equals(w2));
    }
}