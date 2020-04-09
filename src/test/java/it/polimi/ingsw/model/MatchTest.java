package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    final Player p1 = new Player("Pippo", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Pluto", LocalDate.now(), Color.GREY);
    final Player p3 = new Player("Hello", LocalDate.now(), Color.BROWN);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void playGameTurnWithoutCard() throws IOException {
        Battlefield b = Battlefield.getBattlefieldInstance();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Apollo"));
        p2.setPlayerCard(d.getDivinityCard("Apollo"));

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

        Turn t = m.generateTurn(true);

        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        t.moveWorker(m.getSelectedWorker(),1,1 );
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        t.buildBlock(m.getSelectedWorker(), 0, 1);
        t.passTurn();

        assertFalse(Battlefield.getBattlefieldInstance().getCell(0,0).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(0,4).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,1).isWorkerPresent());
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(0, 1).getTower().getHeight());

        assertThrows(RuntimeException.class, ()->m.setSelectedWorker(w1));

        //Play Pluto
        m.setSelectedWorker(w2);
        t = m.generateTurn(true);
        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));
        t.moveWorker(m.getSelectedWorker(),1,4 );
        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        t.buildBlock(m.getSelectedWorker(), 2, 4);
        t.passTurn();

        assertFalse(Battlefield.getBattlefieldInstance().getCell(0,4).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,4).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,1).isWorkerPresent());
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(2, 4).getTower().getHeight());
        Battlefield.getBattlefieldInstance().cleanField();
    }

    @Test
    void playGameTurnWithoutCardTowerLevel() throws IOException {

        Battlefield b = Battlefield.getBattlefieldInstance();

        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Apollo"));
        p2.setPlayerCard(d.getDivinityCard("Apollo"));

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
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addNextBlock();
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addNextBlock();
        //Level 3 Tower
        Battlefield.getBattlefieldInstance().getCell(1,0).getTower().addNextBlock();
        Battlefield.getBattlefieldInstance().getCell(1,0).getTower().addNextBlock();
        Battlefield.getBattlefieldInstance().getCell(1,0).getTower().addNextBlock();
        Turn t = m.generateTurn(true);
        Cell[][] before = Battlefield.getBattlefieldInstance().getWorkerView(w1,
                (cell)->!cell.isWorkerPresent() && Battlefield.getBattlefieldInstance().getCell(m.getSelectedWorker().getRowWorker(), m.getSelectedWorker().getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight());
        int nCell = 0;

        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (before[i][j] != null) nCell++;
            }
        }
        assertEquals(1, nCell);

        m.getSelectedWorker().setWorkerView(t.generateMovementMatrix(m.getSelectedWorker()));

        t.moveWorker(m.getSelectedWorker(),1,1 );

        m.getSelectedWorker().setWorkerView(t.generateBuildingMatrix(m.getSelectedWorker()));
        t.buildBlock(m.getSelectedWorker(), 1,0);

        assertFalse(Battlefield.getBattlefieldInstance().getCell(0,0).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(0,4).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,1).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,0).getTower().isCompleted());
        Battlefield.getBattlefieldInstance().cleanField();
    }

    @Test
    void testPlayersManagement() throws IOException {

        Battlefield b = Battlefield.getBattlefieldInstance();

        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Apollo"));
        p2.setPlayerCard(d.getDivinityCard("Apollo"));

        List<Player> players = new ArrayList<>();
        Match m = new Match(players,new ArrayList<>());
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);
        w2.setWorkerPosition(0,4);

        m.addPlayer(p1);
        assertThrows(RuntimeException.class, ()-> m.addPlayer(p1));
        m.removePlayer(p1);
        m.addPlayer(p1);
        m.addPlayer(p2);
        m.addPlayer(p3);
        m.nextPlayer();

        //set next player with worker
        m.setCurrentPlayer(p2);
        m.setSelectedWorker(w2);

        //control worker & player colors
        assertEquals(Color.GREY, p2.getPlayerColor());
        assertEquals(Color.GREY, w2.getWorkerColor());
        //remove worker
        Battlefield.getBattlefieldInstance().getCell(0,4).removeWorker();
        assertFalse(Battlefield.getBattlefieldInstance().getCell(0,4).isWorkerPresent());

        //clean battlefield for next tests
        Battlefield.getBattlefieldInstance().cleanField();
    }
}