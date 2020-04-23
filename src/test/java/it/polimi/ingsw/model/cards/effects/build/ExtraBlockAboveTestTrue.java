package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.*;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtraBlockAboveTestTrue {

    final Player p1 = new Player("PlayerHephaestus",  Color.BLUE);
    final Player p2 = new Player("PlayerDummy",  Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void extraAboveTrue() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Hephaestus"));
        p2.setPlayerCard(d.getDivinityCard("Atlas"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        w2.setWorkerPosition(4,4);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.LEVEL_3);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - PlayerHephaestus
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,2);
        //3. Build() again
        t.buildBlock(m.getSelectedWorker(),2,2);
        //pass
        t.passTurn();
        //CURRENT PLAYER - PlayerDummy
        t.passTurn();
        //CURRENT PLAYER - PlayerHephaestus
        //0. Generate Turn
        m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,2);

        //ASSERTS build 3 blocks in 2,2
        assertEquals(3, battlefield.getCell(2, 2).getTower().getHeight());
        //control workerView
        Cell[][] workerView = w1.getWorkerView();
        assertNull(workerView[0][0]);
        assertNull(workerView[0][1]);
        assertNull(workerView[0][2]);
        assertNull(workerView[1][0]);
        assertNull(workerView[1][1]);
        assertNull(workerView[2][0]);
        assertNull(workerView[1][2]);
        assertNull(workerView[2][1]);
        assertNull(workerView[2][2]);
        //illegal build of a DOME
        assertThrows(RuntimeException.class, ()->t.buildBlock(m.getSelectedWorker(),2,2));

        battlefield.cleanField();
    }

    @Test
    void extraAboveTrueException() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Hephaestus"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - PlayerHephaestus
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,2);

        //ASSERTS
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(m.getSelectedWorker(),2,1));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }
}