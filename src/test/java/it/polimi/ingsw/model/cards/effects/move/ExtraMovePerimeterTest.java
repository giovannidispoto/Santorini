package it.polimi.ingsw.model.cards.effects.move;

import com.google.gson.JsonElement;
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

class ExtraMovePerimeterTest  {

    final Player p1 = new Player("Jeff Bezos", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);

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

        assertTrue(t.getMovesLeft()==1);
        assertTrue(battlefield.getCell(0,1).getWorker().equals(w1));

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

        assertTrue(t.getMovesLeft()==0);
        assertTrue(battlefield.getCell(2,2).getWorker().equals(w1));

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
}