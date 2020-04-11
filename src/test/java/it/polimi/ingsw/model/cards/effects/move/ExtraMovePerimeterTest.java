package it.polimi.ingsw.model.cards.effects.move;

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

class ExtraMovePerimeterTest  {

    final Player p1 = new Player("PlayerTriton", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("PlayerDummy", LocalDate.now(), Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void extraMovePerimeterCellTrue() throws IOException {
        //Preliminary stuff
        DeckReader reader = new DeckReader();
        Deck deck = reader.loadDeck(new FileReader("src/Divinities.json"));
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        p1.setPlayerCard(deck.getDivinityCard("Triton"));
        List<Player> players = new ArrayList<>();
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Jeff Bezos
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),0,0);
        //4. Move() again
        t.moveWorker(m.getSelectedWorker(),0,1);

        assertEquals(1, t.getMovesLeft());
        assertEquals(battlefield.getCell(0, 1).getWorker(), w1);

        battlefield.cleanField();
    }

    @Test
    void extraMovePerimeterCellFalse() throws IOException {
        //Preliminary stuff
        DeckReader reader = new DeckReader();
        Deck deck = reader.loadDeck(new FileReader("src/Divinities.json"));
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        p1.setPlayerCard(deck.getDivinityCard("Triton"));
        List<Player> players = new ArrayList<>();
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Jeff Bezos
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),2,2);

        assertEquals(0, t.getMovesLeft());
        assertEquals(battlefield.getCell(2, 2).getWorker(), w1);

        battlefield.cleanField();
    }

    @Test
    void extraMovePerimeterCellException() throws IOException {
        //Preliminary stuff
        DeckReader reader = new DeckReader();
        Deck deck = reader.loadDeck(new FileReader("src/Divinities.json"));
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        p1.setPlayerCard(deck.getDivinityCard("Triton"));
        List<Player> players = new ArrayList<>();
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Jeff Bezos
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),2,2);

        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.moveWorker(m.getSelectedWorker(),3,3));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }

    //general purpose test
    @Test
    void extensiveTurnTest() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Triton"));
        p2.setPlayerCard(d.getDivinityCard("Chronus"));
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

        //Simulation : CURRENT PLAYER - PlayerTriton
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
        assertNull(workerView[1][4]);   //Worker w3
        assertNull(workerView[2][2]);   //Dome
        assertNotNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,2);
        //control workerView (MovementMatrix)
        workerView = w2.getWorkerView();
        assertNotNull(workerView[0][1]);
        assertNull(workerView[0][2]);    //Worker w2 at level 3
        assertNotNull(workerView[0][3]);
        assertNotNull(workerView[1][1]);
        assertNull(workerView[1][2]);   //Worker w1
        assertNotNull(workerView[1][3]);
        //special move
        t.moveWorker(m.getSelectedWorker(),0,1);
        //control workerView (MovementMatrix)
        workerView = w2.getWorkerView();
        assertNotNull(workerView[0][0]);
        assertNull(workerView[0][1]);   //Worker w2
        assertNull(workerView[0][2]);   //to high
        assertNotNull(workerView[1][0]);
        assertNotNull(workerView[1][1]);
        assertNull(workerView[1][2]);   //Worker w1
        //special move
        t.moveWorker(m.getSelectedWorker(),1,1);
        //5. CheckLocalWin
        t.checkLocalCondition(m.getSelectedWorker());
        //6. Generate Building Matrix
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        //control workerView (BuildingMatrix)
        workerView = w2.getWorkerView();
        assertNotNull(workerView[0][0]);
        assertNotNull(workerView[0][1]);
        assertNotNull(workerView[0][2]);
        assertNotNull(workerView[1][0]);
        assertNull(workerView[1][1]);    //Worker w2
        assertNull(workerView[1][2]);   //Worker w1
        assertNotNull(workerView[2][0]);
        assertNotNull(workerView[2][1]);
        assertNull(workerView[2][2]);   //Dome
        //7. Build()
        Turn finalT = t;
        assertThrows(RuntimeException.class, ()-> finalT.buildBlock(m.getSelectedWorker(),2,2));
        t.buildBlock(m.getSelectedWorker(),0,2);
        //8. CheckGlobalWin
        //9. PassTurn


        /*EXPECTED Battlefield
        ╔══╦════╦══════╦═══╦════╗
        ║  ║    ║   4  ║   ║    ║
        ╠══╬════╬══════╬═══╬════╣
        ║  ║ w2 ║ 2|w1 ║ 3 ║ w3 ║
        ╠══╬════╬══════╬═══╬════╣
        ║  ║    ║   D  ║   ║    ║
        ╠══╬════╬══════╬═══╬════╣
        ║  ║    ║      ║   ║    ║
        ╠══╬════╬══════╬═══╬════╣
        ║  ║    ║      ║   ║    ║
        ╚══╩════╩══════╩═══╩════╝
         */

        //Control correct Turn Completed
        //w2 has build
        assertEquals(0, t.getBlocksLeft());
        //w2 has moved
        assertEquals(0, t.getMovesLeft());

        assertNull(m.winner);//Check if p1 with triton isn't the winner
        assertEquals(w2, battlefield.getCell(1, 1).getWorker());    //check position
        assertEquals(w1, battlefield.getCell(1, 2).getWorker());    //check position
        assertEquals(w3, battlefield.getCell(1, 4).getWorker());    //check position
        assertEquals(4, battlefield.getCell(0, 2).getTower().getHeight());  //check tower
        assertEquals(0, battlefield.getCell(1, 1).getTower().getHeight());  //check tower
        assertEquals(2, battlefield.getCell(1, 2).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 3).getTower().getHeight());  //check tower
        assertEquals(0, battlefield.getCell(1, 4).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(2, 2).getTower().getHeight());  //check tower
        assertEquals(p1, m.getSelectedWorker().getOwnerWorker());

        //Simulation : CURRENT PLAYER - PlayerTriton
        //0. Generate Turn
        t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //control workerView (MovementMatrix)
        workerView = w1.getWorkerView();
        assertNotNull(workerView[0][1]);
        assertNull(workerView[0][2]);   //Full Tower
        assertNotNull(workerView[0][3]);
        assertNull(workerView[1][1]);   //Worker w2
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
        assertNull(workerView[0][2]);   //Full Tower
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
        ╔══╦════╦═══╦══════╦════╗
        ║  ║    ║ 4 ║      ║    ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║ w2 ║ 2 ║ 3|w1 ║ w3 ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║    ║ D ║   1  ║    ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║    ║   ║      ║    ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║    ║   ║      ║    ║
        ╚══╩════╩═══╩══════╩════╝
         */

        //Control correct Turn Completed
        //w1 has build
        assertEquals(0, t.getBlocksLeft());
        //w1 has moved
        assertEquals(0, t.getMovesLeft());

        assertEquals(p1, m.winner);//Check if p1 with triton is the winner
        assertEquals(w1, battlefield.getCell(1, 3).getWorker());    //check position
        assertEquals(w2, battlefield.getCell(1, 1).getWorker());    //check position
        assertEquals(w3, battlefield.getCell(1, 4).getWorker());    //check position
        assertEquals(4, battlefield.getCell(0, 2).getTower().getHeight());  //check tower
        assertEquals(0, battlefield.getCell(1, 1).getTower().getHeight());  //check tower
        assertEquals(2, battlefield.getCell(1, 2).getTower().getHeight());  //check tower
        assertEquals(3, battlefield.getCell(1, 3).getTower().getHeight());  //check tower
        assertEquals(0, battlefield.getCell(1, 4).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(2, 2).getTower().getHeight());  //check tower
        assertEquals(1, battlefield.getCell(2, 3).getTower().getHeight());  //check tower
        //illegal move
        Turn finalT2 = t;
        assertThrows(RuntimeException.class, ()-> finalT2.moveWorker(m.getSelectedWorker(),1,4));

        //Simulation : CURRENT PLAYER - PlayerTriton
        //0. Generate Turn
        t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w2);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //control workerView (MovementMatrix)
        workerView = w2.getWorkerView();
        assertNotNull(workerView[0][0]);
        assertNotNull(workerView[0][1]);
        assertNull(workerView[0][2]);   //Full Tower too high
        assertNotNull(workerView[1][0]);
        assertNull(workerView[1][1]);    //Worker w2
        assertNull(workerView[1][2]);   //too high
        assertNotNull(workerView[2][0]);
        assertNotNull(workerView[2][1]);
        assertNull(workerView[2][2]);   //Dome
        /*Expected Battlefield
        ╔══╦════╦═══╦══════╦════╗
        ║  ║    ║ 4 ║      ║    ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║ w2 ║ 2 ║ 3|w1 ║ w3 ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║    ║ D ║   1  ║    ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║    ║   ║      ║    ║
        ╠══╬════╬═══╬══════╬════╣
        ║  ║    ║   ║      ║    ║
        ╚══╩════╩═══╩══════╩════╝
         */

        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}