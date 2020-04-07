package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtraBlockPerimetralTest {
    final Player p1 = new Player("PlayerHestia", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);
    final DeckReader reader = new DeckReader();

    @Test
    void buildBlock() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Hestia"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - PlayerHestia
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),0,0);
        //3.b Build()
        t.buildBlock(m.getSelectedWorker(),1,2);

        //ASSERT : We expect two builds, but the second with a different WorkerView (no perimeterCells)
        assertEquals(1, battlefield.getCell(0, 0).getTower().getHeight());
        assertEquals(1, battlefield.getCell(1, 2).getTower().getHeight());
        //control workerView
        Cell[][] workerView = w1.getWorkerView();
        assertNull(workerView[0][0]);
        assertNull(workerView[0][1]);
        assertNull(workerView[0][2]);
        assertNull(workerView[1][0]);
        assertNull(workerView[1][1]);
        assertNull(workerView[2][0]);
        assertNotNull(workerView[1][2]);
        assertNotNull(workerView[2][1]);
        assertNotNull(workerView[2][2]);
        //illegal build
        assertThrows(RuntimeException.class, ()->t.buildBlock(m.getSelectedWorker(),0,1));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}