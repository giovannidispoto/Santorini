package it.polimi.ingsw.model.cards.effects.remove;

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

class RemoveBlockTest {
    final Player p1 = new Player("PlayerAres",  Color.BLUE);
    final Player p2 = new Player("PlayerDummy", Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void removeBlock() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Ares"));
        p2.setPlayerCard(d.getDivinityCard("Atlas"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        List<Worker> workersPlayer1 = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workersPlayer1.add(w1);
        workersPlayer1.add(w2);
        p1.setPlayerWorkers(workersPlayer1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        w2.setWorkerPosition(3,3);
        w3.setWorkerPosition(4,3);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(3,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(3,2).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(4,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.DOME);
        Battlefield.getBattlefieldInstance().getCell(4,2).getTower().addBlock(Block.DOME);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);
        /* Battlefield
        ╔══╦════╦═══╦══════╦═══╗
        ║  ║    ║   ║      ║   ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║ w1 ║   ║      ║   ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║    ║   ║      ║ 3 ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║    ║ 2 ║  w2  ║   ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║    ║ D ║ 2|w3 ║ 4 ║
        ╚══╩════╩═══╩══════╩═══╝
         */

        //Simulation : CURRENT PLAYER - PlayerAres
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,1);
        //5. CheckLocalWin...
        //6. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //7. Build()
        t.buildBlock(m.getSelectedWorker(),0,0);
        //8. CheckGlobalWin...
        //Special Effect Ares
        //9. Change Worker
        m.setSelectedWorker(t.changeWorkerPlayer(m.getSelectedWorker()));
        //10. Generate Remove Matrix
        m.getSelectedWorker().setWorkerView(t.generateRemoveMatrix(m.getSelectedWorker()));
        //11. RemoveBlock()
        t.removeBlock(m.getSelectedWorker(),2,4);
        //12. PassTurn
        //t.passTurn();

        //ASSERT : We expect this towers & Heights, the 2nd worker with a different WorkerView
        /*EXPECTED Battlefield
        ╔═══╦════╦═════╦══════╦═══╗
        ║ 1 ║ w1 ║     ║      ║   ║
        ╠═══╬════╬═════╬══════╬═══╣
        ║   ║    ║     ║      ║   ║
        ╠═══╬════╬═════╬══════╬═══╣
        ║   ║    ║     ║      ║ 2 ║
        ╠═══╬════╬═════╬══════╬═══╣
        ║   ║    ║ 2   ║ 0|w2 ║   ║
        ╠═══╬════╬═════╬══════╬═══╣
        ║   ║    ║ 1|D ║ 2|w3 ║ 4 ║
        ╚═══╩════╩═════╩══════╩═══╝
         */
        //Control correct Turn Completed
        //w1 has build
        assertEquals(0, t.getBlocksLeft());
        //w1 has moved
        assertEquals(0, t.getMovesLeft());

        assertEquals(w1, battlefield.getCell(0, 1).getWorker());
        assertEquals(w2, battlefield.getCell(3, 3).getWorker());
        assertEquals(w3, battlefield.getCell(4, 3).getWorker());
        assertEquals(1, battlefield.getCell(0, 0).getTower().getHeight());
        assertEquals(2, battlefield.getCell(2, 4).getTower().getHeight());
        assertEquals(2, battlefield.getCell(3, 2).getTower().getHeight());
        assertEquals(0, battlefield.getCell(3, 3).getTower().getHeight());
        assertEquals(1, battlefield.getCell(4, 2).getTower().getHeight());
        assertEquals(Block.DOME, battlefield.getCell(4, 2).getTower().getLastBlock());
        assertEquals(2, battlefield.getCell(4, 3).getTower().getHeight());
        assertEquals(4, battlefield.getCell(4, 4).getTower().getHeight());

        /*EXPECTED WorkerView W2
        ╔══╦══╦═══╦══╦═══╗
        ║  ║  ║   ║  ║   ║
        ╠══╬══╬═══╬══╬═══╣
        ║  ║  ║   ║  ║   ║
        ╠══╬══╬═══╬══╬═══╣
        ║  ║  ║   ║  ║ v ║
        ╠══╬══╬═══╬══╬═══╣
        ║  ║  ║ v ║  ║   ║
        ╠══╬══╬═══╬══╬═══╣
        ║  ║  ║   ║  ║   ║
        ╚══╩══╩═══╩══╩═══╝
         */
        //control workerView
        Cell[][] workerView = w2.getWorkerView();
        assertNull(workerView[2][2]);
        assertNull(workerView[2][3]);
        assertNotNull(workerView[2][4]);
        assertNotNull(workerView[3][2]);
        assertNull(workerView[3][3]);
        assertNull(workerView[3][4]);
        assertNull(workerView[4][2]);
        assertNull(workerView[4][3]);
        assertNull(workerView[4][4]);
        //illegal remove
        assertThrows(RuntimeException.class, ()->t.removeBlock(m.getSelectedWorker(),4,4));
        //illegal move
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),4,4));
        //illegal build
        assertThrows(RuntimeException.class, ()->t.buildBlock(m.getSelectedWorker(),4,4));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    @Test
    void removeBlockSubTest() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Ares"));
        p2.setPlayerCard(d.getDivinityCard("Atlas"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        List<Worker> workersPlayer1 = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        workersPlayer1.add(w1);
        workersPlayer1.add(w2);
        p1.setPlayerWorkers(workersPlayer1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        w2.setWorkerPosition(3,3);
        w3.setWorkerPosition(2,3);
        //Add blocks
        //Tower 2 in 3,2
        Battlefield.getBattlefieldInstance().getCell(3,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(3,3).getTower().addBlock(Block.LEVEL_2);
        //Tower 3 in 2,4
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(2,4).getTower().addBlock(Block.LEVEL_3);
        //Tower 2 in 4,3
        Battlefield.getBattlefieldInstance().getCell(4,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,3).getTower().addBlock(Block.LEVEL_2);
        //Tower 4 in 4,4
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.DOME);
        //Dome in 4,2
        Battlefield.getBattlefieldInstance().getCell(4,2).getTower().addBlock(Block.DOME);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);
        /* Battlefield
        ╔══╦════╦═══╦══════╦═══╗
        ║  ║    ║   ║      ║   ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║ w1 ║   ║      ║   ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║    ║   ║  w3  ║ 3 ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║    ║   ║ 2|w2 ║   ║
        ╠══╬════╬═══╬══════╬═══╣
        ║  ║    ║ D ║   2  ║ 4 ║
        ╚══╩════╩═══╩══════╩═══╝
         */

        //Simulation : CURRENT PLAYER - PlayerAres
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w2);
        //2. Generate Movement Matrix
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        //3. Check GlobalEffect
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),2,4);
        //5. CheckLocalWin
        assertNull(m.winner);
        t.checkLocalCondition(m.getSelectedWorker());

        /*EXPECTED Battlefield
        ╔══╦════╦═══╦════╦══════╗
        ║  ║    ║   ║    ║      ║
        ╠══╬════╬═══╬════╬══════╣
        ║  ║ w1 ║   ║    ║      ║
        ╠══╬════╬═══╬════╬══════╣
        ║  ║    ║   ║ w3 ║ 3|w2 ║
        ╠══╬════╬═══╬════╬══════╣
        ║  ║    ║   ║  2 ║      ║
        ╠══╬════╬═══╬════╬══════╣
        ║  ║    ║ D ║  2 ║   4  ║
        ╚══╩════╩═══╩════╩══════╝
         */
        //FirstAssertion
        assertEquals(p1, m.winner);//Check if p1 with pan it's the winner
        assertEquals(w2, battlefield.getCell(2, 4).getWorker());    //check position
        assertEquals(3, battlefield.getCell(2, 4).getTower().getHeight());  //check build
        //Control correct Turn NotCompleted
        //w2 hasn't build
        assertEquals(1, t.getBlocksLeft());
        //w2 has moved
        assertEquals(0, t.getMovesLeft());

        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}