package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.TestsStaticResources.absPathDivinitiesCardsDeck;
import static org.junit.jupiter.api.Assertions.*;

class PushCharacterTest {

    final Player p1 = new Player("Player1",  Color.BLUE);
    final Player p2 = new Player("Player2", Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();


    @Test
    void basicMove() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        p2.setPlayerCard(d.getDivinityCard("MINOTAUR"));
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
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),4,0);

        //Check if the worker has changed position
        assertFalse(Battlefield.getBattlefieldInstance().getCell(4,1).isWorkerPresent());
        assertEquals(battlefield.getCell(4, 0).getWorker(), w1);
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //test push enemy and win
    @Test
    void pushEnemyTrue() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        p2.setPlayerCard(d.getDivinityCard("MINOTAUR"));
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
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
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
        assertEquals(3, Battlefield.getBattlefieldInstance().getCell(1, 3).getTower().getHeight());
        assertEquals(2, Battlefield.getBattlefieldInstance().getCell(2, 2).getTower().getHeight());
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //Test illegal moves = False
    //test enemy can't move, because have a worker behind
    @Test
    void pushEnemy1False() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        p2.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(0, 2);
        w2.setWorkerPosition(0, 0);
        //enemy
        w3.setWorkerPosition(0, 1);
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //Assertion : move to 0,1 illegal
        assertEquals(battlefield.getCell(0, 2).getWorker(), w1);
        assertEquals(battlefield.getCell(0, 0).getWorker(), w2);
        //enemy
        assertEquals(battlefield.getCell(0, 1).getWorker(), w3);
        //control workerView
        Cell[][] workerView =w1.getWorkerView();
        assertNull(workerView[0][1]);
        //illegal move
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),0,1));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //test enemy in perimeter
    @Test
    void pushEnemy2False() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        p2.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(4, 3);
        w3.setWorkerPosition(4, 4);
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //Assertion : move to 4,4 illegal
        assertEquals(battlefield.getCell(4, 3).getWorker(), w1);
        assertEquals(battlefield.getCell(4, 4).getWorker(), w3);
        //control workerView
        Cell[][] workerView =w1.getWorkerView();
        assertNull(workerView[4][4]);
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),4,4));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //test enemy that can't move, because have a dome behind
    @Test
    void pushEnemy3False() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        p2.setPlayerCard(d.getDivinityCard("MINOTAUR"));
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
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //Assertion : move to 2,1 illegal
        assertEquals(battlefield.getCell(2, 2).getWorker(), w1);
        assertEquals(battlefield.getCell(2, 1).getWorker(), w3);
        //control workerView
        Cell[][] workerView =w1.getWorkerView();
        assertNull(workerView[2][1]);
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),2,1));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //general purpose test
    @Test
    void extensiveTurnTest() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        p2.setPlayerCard(d.getDivinityCard("ATLAS"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,2);
        w2.setWorkerPosition(1,3);
        w3.setWorkerPosition(1,4);
        //Add blocks
        //Tower 2 in 1,2
        Battlefield.getBattlefieldInstance().getCell(1,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(1,2).getTower().addBlock(Block.LEVEL_2);
        //Tower 3 in 1,3
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_3);
        //Tower 3 in 0,2
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_3);
        //Dome
        Battlefield.getBattlefieldInstance().getCell(2,2).getTower().addBlock(Block.DOME);
        Match m = new Match(players);
        m.setCurrentPlayer(p1);
        /* Battlefield
        ╔══╦══╦══════╦══════╦════╗
        ║  ║  ║   3  ║      ║    ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║ 2|w1 ║ 3|w2 ║ w3 ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║   D  ║      ║    ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║      ║      ║    ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║      ║      ║    ║
        ╚══╩══╩══════╩══════╩════╝
         */

        //Simulation : CURRENT PLAYER - PlayerMINOTAUR
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w2);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //control workerView (MovementMatrix)
        Cell [][] workerView = w2.getWorkerView();
        assertNotNull(workerView[0][2]);
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[0][4]);
        assertNull(workerView[1][2]);   //Worker w1
        assertNull(workerView[1][3]);   //Worker w2
        assertNull(workerView[1][4]);   //Worker w3 than can't be pushed
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,2);
        //5. CheckLocalWin
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //control workerView (BuildingMatrix)
        workerView = w2.getWorkerView();
        assertNotNull(workerView[0][1]);
        assertNull(workerView[0][2]);    //Worker w2 at level 3
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[1][1]);
        assertNull(workerView[1][2]);   //Worker w1
        assertNotNull(workerView[1][3]);
        //7. Build()
        Turn finalT = t;
        assertThrows(RuntimeException.class, ()-> finalT.buildBlock(m.getSelectedWorker(),0,2));
        t.buildBlock(m.getSelectedWorker(),0,1);
        //8. CheckGlobalWin
        //9. PassTurn


        /*EXPECTED Battlefield
        ╔══╦══╦══════╦═══╦════╗
        ║  ║1 ║ 3|w2 ║   ║    ║
        ╠══╬══╬══════╬═══╬════╣
        ║  ║  ║ 2|w1 ║ 3 ║ w3 ║
        ╠══╬══╬══════╬═══╬════╣
        ║  ║  ║   D  ║   ║    ║
        ╠══╬══╬══════╬═══╬════╣
        ║  ║  ║      ║   ║    ║
        ╠══╬══╬══════╬═══╬════╣
        ║  ║  ║      ║   ║    ║
        ╚══╩══╩══════╩═══╩════╝
         */

        //Control correct Turn Completed
        //w2 has build
        assertEquals(0, t.getBlocksLeft());
        //w2 has moved
        assertEquals(0, t.getMovesLeft());

        assertNull(m.winner);//Check if p1 with MINOTAUR isn't the winner
        assertEquals(w2, battlefield.getCell(0, 2).getWorker());    //check position
        assertEquals(3, battlefield.getCell(0, 2).getTower().getHeight());  //check tower
        assertEquals(p1, m.getSelectedWorker().getOwnerWorker());

        //Simulation : CURRENT PLAYER - PlayerMINOTAUR
        //0. Generate Turn
        t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //control workerView (MovementMatrix)
        workerView = w1.getWorkerView();
        assertNotNull(workerView[0][1]);
        assertNull(workerView[0][2]);   //Worker w2
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[1][1]);
        assertNull(workerView[1][2]);   //Worker w1
        assertNotNull(workerView[1][3]);
        assertNotNull(workerView[2][1]);
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),1,3);
        //5. CheckLocalWin
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //control workerView (BuildingMatrix)
        workerView = w1.getWorkerView();
        assertNull(workerView[0][2]);   //Worker w2
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[0][4]);
        assertNotNull(workerView[1][2]);
        assertNull(workerView[1][3]);   //Worker w1
        assertNull(workerView[1][4]);   //Worker w3
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        //7. Build()
        t.buildBlock(m.getSelectedWorker(),2,3);

        /*EXPECTED Battlefield
        ╔══╦══╦══════╦══════╦════╗
        ║  ║1 ║ 3|w2 ║      ║    ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║   2  ║ 3|w1 ║ w3 ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║   D  ║  1   ║    ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║      ║      ║    ║
        ╠══╬══╬══════╬══════╬════╣
        ║  ║  ║      ║      ║    ║
        ╚══╩══╩══════╩══════╩════╝
         */

        //Control correct Turn Completed
        //w1 has build
        assertEquals(0, t.getBlocksLeft());
        //w1 has moved
        assertEquals(0, t.getMovesLeft());

        assertEquals(p1, m.winner);//Check if p1 with MINOTAUR is the winner
        assertEquals(w1, battlefield.getCell(1, 3).getWorker());    //check position
        assertEquals(w3, battlefield.getCell(1, 4).getWorker());    //check position
        assertEquals(3, battlefield.getCell(1, 3).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(2, 3).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(0, 1).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(0, 2).getTower().getHeight());  //check ground
        assertEquals(2, battlefield.getCell(1, 2).getTower().getHeight());  //check tower
        assertEquals(0, battlefield.getCell(1, 4).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(2, 2).getTower().getHeight());  //check dome
        //illegal move
        Turn finalT2 = t;
        assertThrows(RuntimeException.class, ()-> finalT2.moveWorker(m.getSelectedWorker(),1,4));

        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //general purpose test with MINOTAUR effect
    @Test
    void MINOTAURTurnTest() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("CHRONUS"));
        p2.setPlayerCard(d.getDivinityCard("MINOTAUR"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,2);
        w2.setWorkerPosition(1,3);
        w3.setWorkerPosition(1,4);
        //Add blocks
        //Tower 2 in 1,2
        Battlefield.getBattlefieldInstance().getCell(1,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(1,2).getTower().addBlock(Block.LEVEL_2);
        //Tower 3 in 1,3
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(1,3).getTower().addBlock(Block.LEVEL_3);
        //Tower 2 in 1,4
        Battlefield.getBattlefieldInstance().getCell(1,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(1,4).getTower().addBlock(Block.LEVEL_2);
        //Tower 3 in 0,2
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_3);
        //Dome
        Battlefield.getBattlefieldInstance().getCell(2,2).getTower().addBlock(Block.DOME);
        Match m = new Match(players);
        m.setCurrentPlayer(p1);
        /* Battlefield
        ╔══╦══╦══════╦══════╦══════╗
        ║  ║  ║   3  ║      ║      ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║ 2|w1 ║ 3|w2 ║ 2|w3 ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║   D  ║      ║      ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║      ║      ║      ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║      ║      ║      ║
        ╚══╩══╩══════╩══════╩══════╝
         */

        //Simulation : CURRENT PLAYER - PlayerCHRONUS
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //control workerView (MovementMatrix)
        Cell[][] workerView = w1.getWorkerView();
        assertNotNull(workerView[0][1]);
        assertNotNull(workerView[0][2]);
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[1][1]);
        assertNull(workerView[1][2]);   //Worker w1
        assertNull(workerView[1][3]);    //Worker w2
        assertNotNull(workerView[2][1]);
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,2);
        //5. CheckLocalWin
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //control workerView (BuildingMatrix)
        workerView = w1.getWorkerView();
        assertNotNull(workerView[0][1]);
        assertNull(workerView[0][2]);    //Worker w1 at level 3
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[1][1]);
        assertNotNull(workerView[1][2]);
        assertNull(workerView[1][3]);    //Worker w2
        //7. Build()
        Turn finalT = t;
        assertThrows(RuntimeException.class, ()-> finalT.buildBlock(m.getSelectedWorker(),0,2));
        t.buildBlock(m.getSelectedWorker(),1,2);
        //8. CheckGlobalWin
        //9. PassTurn
        t.passTurn();


        /*EXPECTED Battlefield
        ╔══╦══╦══════╦══════╦══════╗
        ║  ║  ║ 3|w1 ║      ║      ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║   3  ║ 3|w2 ║ 2|w3 ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║   D  ║      ║      ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║      ║      ║      ║
        ╠══╬══╬══════╬══════╬══════╣
        ║  ║  ║      ║      ║      ║
        ╚══╩══╩══════╩══════╩══════╝
         */

        //Control correct Turn Completed
        //w1 has build
        assertEquals(0, t.getBlocksLeft());
        //w1 has moved
        assertEquals(0, t.getMovesLeft());

        assertEquals(p1, m.winner);//Check if p1 with CHRONUS is the winner
        assertEquals(w1, battlefield.getCell(0, 2).getWorker());    //check position
        assertEquals(3, battlefield.getCell(0, 2).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 2).getTower().getHeight());  //check tower
        assertEquals(2, battlefield.getCell(1, 4).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 3).getTower().getHeight());  //check tower
        assertFalse(battlefield.getCell(1, 2).isWorkerPresent());  //check not worker
        assertTrue(battlefield.getCell(1, 3).isWorkerPresent());  //check worker w2
        assertEquals(p1, m.getSelectedWorker().getOwnerWorker());

        //Simulation : CURRENT PLAYER - PlayerMINOTAUR
        //0. Generate Turn
        t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w3);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //control workerView (MovementMatrix)
        workerView = w3.getWorkerView();
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[0][4]);
        assertNotNull(workerView[1][3]);   //Worker w2 can be pushed
        assertNull(workerView[1][4]);   //Worker w3
        assertNotNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),1,3);
        //5. CheckLocalWin
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //control workerView (BuildingMatrix)
        workerView = w3.getWorkerView();
        assertNull(workerView[0][2]);   //Worker w1
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[0][4]);
        assertNull(workerView[1][2]);   //Worker w2
        assertNull(workerView[1][3]);   //Worker w3
        assertNotNull(workerView[1][4]);
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        //7. Build()
        t.buildBlock(m.getSelectedWorker(),1,4);

        /*EXPECTED Battlefield
        ╔══╦══╦══════╦══════╦═══╗
        ║  ║  ║ 3|w1 ║      ║   ║
        ╠══╬══╬══════╬══════╬═══╣
        ║  ║  ║ 3|w2 ║ 3|w3 ║ 3 ║
        ╠══╬══╬══════╬══════╬═══╣
        ║  ║  ║   D  ║      ║   ║
        ╠══╬══╬══════╬══════╬═══╣
        ║  ║  ║      ║      ║   ║
        ╠══╬══╬══════╬══════╬═══╣
        ║  ║  ║      ║      ║   ║
        ╚══╩══╩══════╩══════╩═══╝
         */

        //Control correct Turn Completed
        //w3 has build
        assertEquals(0, t.getBlocksLeft());
        //w3 has moved
        assertEquals(0, t.getMovesLeft());

        assertEquals(p2, m.winner);//Check if p2 with MINOTAUR is the winner
        assertEquals(w1, battlefield.getCell(0, 2).getWorker());    //check position
        assertEquals(w2, battlefield.getCell(1, 2).getWorker());    //check position
        assertEquals(w3, battlefield.getCell(1, 3).getWorker());    //check position
        assertEquals(3, battlefield.getCell(0, 2).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 2).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 3).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 4).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(2, 2).getTower().getHeight());  //check dome
        //illegal move
        Turn finalT2 = t;
        assertThrows(RuntimeException.class, ()-> finalT2.moveWorker(m.getSelectedWorker(),2,2));

        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}