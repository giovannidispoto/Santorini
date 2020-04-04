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
class PushCharacterTest {

    final Player p1 = new Player("Player1", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Player2", LocalDate.now(), Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);

    @Test
    void basicMove() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(4,1);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(4,0).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,0).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,0).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(4,1).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,1).getTower().addBlock(Block.LEVEL_2);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),4,0);

        //Check if the workers have changed position
        assertFalse(Battlefield.getBattlefieldInstance().getCell(4,1).isWorkerPresent());
        assertEquals(battlefield.getCell(4, 0).getWorker(), w1);
    }

    //test push enemy and win
    @Test
    void pushEnemy1True() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(2,2);
        w3.setWorkerPosition(1,3);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(2,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(2,2).getTower().addBlock(Block.LEVEL_2);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),1,3);

        //Check if the workers have changed position and all is ok
        assertFalse(Battlefield.getBattlefieldInstance().getCell(2,2).isWorkerPresent());
        assertEquals(battlefield.getCell(1, 3).getWorker(), w1);
        assertEquals(battlefield.getCell(0, 4).getWorker(), w3);
        assertEquals(2, Battlefield.getBattlefieldInstance().getCell(0, 4).getTower().getHeight());
    }

    //Test illegal moves = False
    //test enemy can't move, because have a worker behind
    @Test
    void pushEnemy2False() {
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
        w1.setWorkerPosition(0, 2);
        w2.setWorkerPosition(0, 1);
        w3.setWorkerPosition(0, 0);
        Match m = new Match(players, new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //Assertion : move to 0,1 illegal
        assertEquals(battlefield.getCell(0, 0).getWorker(), w3);
        Cell[][] workerView =w1.getWorkerView();
        assertNull(workerView[0][1]);
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),0,1));
    }

    //test enemy in perimeter
    @Test
    void pushEnemy3False() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(4, 3);
        w3.setWorkerPosition(4, 4);
        Match m = new Match(players, new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //Assertion : move to 4,4 illegal
        assertEquals(battlefield.getCell(4, 4).getWorker(), w3);
        Cell[][] workerView =w1.getWorkerView();
        assertNull(workerView[4][4]);
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),4,4));
    }

    //test enemy that can't move, because have a dome behind
    @Test
    void pushEnemy4False() {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(2, 2);
        w3.setWorkerPosition(2, 1);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(2,0).getTower().addBlock(Block.DOME);
        Match m = new Match(players, new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //Assertion : move to 2,1 illegal
        assertEquals(battlefield.getCell(2, 1).getWorker(), w3);
        Cell[][] workerView =w1.getWorkerView();
        assertNull(workerView[2][1]);
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),2,1));
    }
}