package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    final  Player p1 = new Player("Pippo", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Pluto", LocalDate.now() ,Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p2);

    @Test
    void playGameTurnWithoutCard() {
        Battlefield b = Battlefield.getBattelfieldInstance();

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);
        w2.setWorkerPosition(0,4);

        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Play Pippo
        m.setSelectedWorker(w1);
        Cell[][] before = Battlefield.getBattelfieldInstance().getWorkerView(w1, (cell)->!cell.isWorkerPresent());
        Turn t = m.generateTurn();
        t.moveWorker(m.getSelectedWorker(),1,1 );
        Cell[][] after = Battlefield.getBattelfieldInstance().getWorkerView(w1,(cell)->!cell.isWorkerPresent());
        t.buildBlock(m.getSelectedWorker(), 0, 1);
        t.passTurn();

        assertFalse(Battlefield.getBattelfieldInstance().getCell(0,0).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(0,4).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(1,1).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(0,1).getTower().getHeight() == 1);

        assertThrows(RuntimeException.class, ()->m.setSelectedWorker(w1));

        //Play Pluto
        m.setSelectedWorker(w2);
        before = Battlefield.getBattelfieldInstance().getWorkerView(w1, (cell)->!cell.isWorkerPresent());
        t = m.generateTurn();
        t.moveWorker(m.getSelectedWorker(),4,4 );
        after = Battlefield.getBattelfieldInstance().getWorkerView(w1,(cell)->!cell.isWorkerPresent());
        t.buildBlock(m.getSelectedWorker(), 4, 3);
        t.passTurn();

        assertFalse(Battlefield.getBattelfieldInstance().getCell(0,4).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(4,4).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(1,1).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(4,3).getTower().getHeight() == 1);
        Battlefield.getBattelfieldInstance().cleanField();
    }

    @Test
    void playGameTurnWithoutCardTowerLevel() {

        Battlefield b = Battlefield.getBattelfieldInstance();

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);
        w2.setWorkerPosition(0,4);

        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);
        m.setSelectedWorker(w1);

        //Building Block near player
        //Level 2 Tower
        Battlefield.getBattelfieldInstance().getCell(0,1).getTower().addNextBlock();
        Battlefield.getBattelfieldInstance().getCell(0,1).getTower().addNextBlock();
        //Level 3 Tower
        Battlefield.getBattelfieldInstance().getCell(1,0).getTower().addNextBlock();
        Battlefield.getBattelfieldInstance().getCell(1,0).getTower().addNextBlock();
        Battlefield.getBattelfieldInstance().getCell(1,0).getTower().addNextBlock();
        Turn t = m.generateTurn();
        Cell[][] before = Battlefield.getBattelfieldInstance().getWorkerView(w1,
                (cell)->!cell.isWorkerPresent() && Battlefield.getBattelfieldInstance().getCell(m.getSelectedWorker().getRowWorker(), m.getSelectedWorker().getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
        int nCell = 0;

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (before[i][j] != null) nCell++;
            }
        }
        assertTrue(nCell == 1);

        t.moveWorker(m.getSelectedWorker(),1,1 );
        Cell[][] after = Battlefield.getBattelfieldInstance().getWorkerView(w1,(cell)->!cell.isWorkerPresent());
        t.buildBlock(m.getSelectedWorker(), 1,0);

        assertFalse(Battlefield.getBattelfieldInstance().getCell(0,0).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(0,4).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(1,1).isWorkerPresent());
        assertTrue(Battlefield.getBattelfieldInstance().getCell(1,0).getTower().isCompleted());
        Battlefield.getBattelfieldInstance().cleanField();
    }
}