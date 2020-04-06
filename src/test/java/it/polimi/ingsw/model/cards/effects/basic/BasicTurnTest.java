package it.polimi.ingsw.model.cards.effects.basic;

import com.google.gson.JsonElement;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicTurnTest {
    final Player p1 = new Player("Bill Gates", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);
    final DeckReader reader = new DeckReader();


    @Test
    void basicUsage() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Apollo"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Bill Gates
        //0. Generate Turn
        Turn t = m.generateTurn(true);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Move()
        t.moveWorker(m.getSelectedWorker(),m.getSelectedWorker().getRowWorker()+1,m.getSelectedWorker().getColWorker()+1);
        //4. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //5. Build()
        t.buildBlock(m.getSelectedWorker(),m.getSelectedWorker().getRowWorker()-1,m.getSelectedWorker().getColWorker()-1);

        //ASSERTs : We expect a new position for the worker and a new block beside him
        assertTrue(battlefield.getCell(2,2).getWorker().equals(w1));
        assertTrue(battlefield.getCell(1,1).getTower().getHeight()==1);

        battlefield.cleanField();
    }

    @Test
    void basicUsageExceptionBuilding() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Apollo"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Bill Gates
        //0. Generate Turn
        Turn t = m.generateTurn(true);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //4. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));

        //ASSERTS
        //Building outside the worker view
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(w1,3,3));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }

    @Test
    void basicUsageExceptionMovement() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Apollo"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Bill Gates
        //0. Generate Turn
        Turn t = m.generateTurn(true);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));

        //ASSERTS
        //Move outside the worker view
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.moveWorker(w1,3,3));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }
}