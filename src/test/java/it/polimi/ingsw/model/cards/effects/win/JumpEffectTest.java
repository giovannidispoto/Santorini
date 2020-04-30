package it.polimi.ingsw.model.cards.effects.win;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JumpEffectTest {
    final Player p1 = new Player("PlayerPAN", Color.BLUE);
    final Player p2 = new Player("PlayerDummy", Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void moveWorker() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("PAN"));
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
        Match m = new Match(players,new ArrayList<>());
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

        //Simulation : CURRENT PLAYER - PlayerPAN
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),1,1);
        //5. CheckLocalWin...
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //7. Build()
        t.buildBlock(m.getSelectedWorker(),0,0);
        //8. CheckGlobalWin...

        //Control correct Turn Completed
        //w1 has build
        assertEquals(0, t.getBlocksLeft());
        //w1 has moved
        assertEquals(0, t.getMovesLeft());

        //12. PassTurn
        t.passTurn();
        //Simulation : CURRENT PLAYER - PlayerDummy
        //0. Generate Turn
        t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w3);


        //FirstAssertion
        assertEquals(p1, m.winner);//Check if p1 with PAN it's the winner
        m.winner=null;//reset
        assertEquals(w1, battlefield.getCell(1, 1).getWorker());    //check position
        assertEquals(1, battlefield.getCell(0, 0).getTower().getHeight());  //check build
        assertEquals(p2, m.getSelectedWorker().getOwnerWorker());
        //control workerView (BuildingMatrix)
        Cell[][] workerView = w1.getWorkerView();
        assertNotNull(workerView[0][0]);
        assertNotNull(workerView[0][1]);
        assertNotNull(workerView[0][2]);
        assertNotNull(workerView[1][0]);
        assertNull(workerView[1][1]);   //Worker w1
        assertNotNull(workerView[1][2]);
        assertNotNull(workerView[2][0]);
        assertNotNull(workerView[2][1]);
        assertNull(workerView[2][2]);   //Dome

        //Simulation : CURRENT PLAYER - PlayerDummy
        //12. PassTurn
        t.passTurn();
        //Simulation : CURRENT PLAYER - PlayerPAN
        //0. Generate Turn
        t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w2);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),2,3);
        //5. CheckLocalWin...
        t.checkLocalCondition(m.getSelectedWorker());

        /*EXPECTED Battlefield
        ╔═══╦════╦═══╦════╦════╗
        ║ 1 ║    ║ 3 ║    ║    ║
        ╠═══╬════╬═══╬════╬════╣
        ║   ║ w1 ║ 2 ║  3 ║ w3 ║
        ╠═══╬════╬═══╬════╬════╣
        ║   ║    ║ D ║ w2 ║    ║
        ╠═══╬════╬═══╬════╬════╣
        ║   ║    ║   ║    ║    ║
        ╠═══╬════╬═══╬════╬════╣
        ║   ║    ║   ║    ║    ║
        ╚═══╩════╩═══╩════╩════╝
         */

        //Second Assertion
        assertEquals(p1, m.winner);//Check if p1 with PAN it's the winner
        assertEquals(p1, m.getSelectedWorker().getOwnerWorker());
        //Check Towers
        assertEquals(1, battlefield.getCell(0, 0).getTower().getHeight());
        assertEquals(3, battlefield.getCell(0, 2).getTower().getHeight());
        assertEquals(0, battlefield.getCell(1, 1).getTower().getHeight());
        assertEquals(2, battlefield.getCell(1, 2).getTower().getHeight());
        assertEquals(3, battlefield.getCell(1, 3).getTower().getHeight());
        assertEquals(0, battlefield.getCell(1, 4).getTower().getHeight());
        assertEquals(1, battlefield.getCell(2, 2).getTower().getHeight());
        assertEquals(Block.DOME, battlefield.getCell(2, 2).getTower().getLastBlock());
        assertEquals(0, battlefield.getCell(2, 3).getTower().getHeight());
        //Check Workers
        assertEquals(w1, battlefield.getCell(1, 1).getWorker());
        assertEquals(w3, battlefield.getCell(1, 4).getWorker());
        assertEquals(w2, battlefield.getCell(2, 3).getWorker());

        //control workerView (MovementMatrix)
        workerView = w2.getWorkerView();
        assertNotNull(workerView[0][2]);
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[0][4]);
        assertNotNull(workerView[1][2]);
        assertNull(workerView[1][3]);   //Worker w2
        assertNull(workerView[1][4]);   //Worker w3
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);

        //w2 hasn't build
        assertEquals(1, t.getBlocksLeft());
        //w2 has moved
        assertEquals(0, t.getMovesLeft());

        //illegal move
        Turn finalT = t;
        assertThrows(RuntimeException.class, ()-> finalT.moveWorker(m.getSelectedWorker(),2,2));
        //illegal build
        assertThrows(RuntimeException.class, ()-> finalT.buildBlock(m.getSelectedWorker(),2,2));

        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    @Test
    void moveWorkerSubTest() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("PAN"));
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
        Match m = new Match(players,new ArrayList<>());
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

        //Simulation : CURRENT PLAYER - PlayerPAN
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,2);
        //5. CheckLocalWin...
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //7. Build()
        t.buildBlock(m.getSelectedWorker(),0,1);
        //8. CheckGlobalWin...
        //12. PassTurn
        t.passTurn();

        /*EXPECTED Battlefield
        ╔══╦═══╦══════╦═══╦════╗
        ║  ║ 1 ║ 3|w1 ║   ║    ║
        ╠══╬═══╬══════╬═══╬════╣
        ║  ║   ║   2  ║ 3 ║ w3 ║
        ╠══╬═══╬══════╬═══╬════╣
        ║  ║   ║   D  ║   ║    ║
        ╠══╬═══╬══════╬═══╬════╣
        ║  ║   ║      ║   ║    ║
        ╠══╬═══╬══════╬═══╬════╣
        ║  ║   ║      ║   ║    ║
        ╚══╩═══╩══════╩═══╩════╝
         */
        //FirstAssertion
        assertEquals(p1, m.winner);//Check if p1 with PAN it's the winner
        assertEquals(w1, battlefield.getCell(0, 2).getWorker());    //check position
        assertEquals(1, battlefield.getCell(0, 1).getTower().getHeight());  //check build
        assertEquals(3, battlefield.getCell(0, 2).getTower().getHeight());  //check build
        assertEquals(w1, m.getSelectedWorker());

        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}