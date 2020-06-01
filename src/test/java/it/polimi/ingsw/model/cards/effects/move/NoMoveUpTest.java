package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.effects.global.NoLevelUpCondition;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoMoveUpTest {
    final Player p1 = new Player("Player1", Color.BLUE);
    final Player p2 = new Player("Player2", Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    //ATHENA Blocks movements && generate exception
    @Test
    void moveWorkerUp() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("ATHENA"));
        p2.setPlayerCard(d.getDivinityCard("ATLAS"));

        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,3);
        w3.setWorkerPosition(3,3);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_1);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,4);
        //5. CheckLocalWin...
        //6. BuildPhase...
        //7. CheckGlobalWin...
        //8. PassTurn
        t.passTurn();
        //CURRENT PLAYER - Player2
        //1. Worker Selection Phase
        m.setSelectedWorker(w3);
        //2. Generate Movement Matrix
        w3.setWorkerView(t.generateMovementMatrix(w3));
        //3. Check GlobalEffect
        w3.setWorkerView(NoLevelUpCondition.getInstance().applyEffect(w3));


        //Check if the workers have changed position
        assertFalse(Battlefield.getBattlefieldInstance().getCell(1,3).isWorkerPresent());
        assertEquals(battlefield.getCell(0, 4).getWorker(), w1);
        assertEquals(battlefield.getCell(3, 3).getWorker(), w3);
        //control workerView
        Cell[][] workerView =w3.getWorkerView();
        assertNull(workerView[4][4]);
        //control Towers Height
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(0, 4).getTower().getHeight());
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(4, 4).getTower().getHeight());
        assertThrows(RuntimeException.class, ()->t.moveWorker(m.getSelectedWorker(),4,4));
        //Clean battlefield for next tests
        battlefield.cleanField();
    }

    //First Turn ATHENA Blocks movement, second turn not
    @Test
    void moveWorkerUPANdNot() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("ATHENA"));
        p2.setPlayerCard(d.getDivinityCard("ATLAS"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,3);
        w3.setWorkerPosition(3,3);
        //Add blocks
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(3,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(3,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(4,4).getTower().addBlock(Block.LEVEL_3);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,4);
        //5. CheckLocalWin...
        //6. BuildPhase...
        //7. CheckGlobalWin...
        //8. PassTurn
        t.passTurn();
        //CURRENT PLAYER - Player2
        //1. Worker Selection Phase
        m.setSelectedWorker(w3);
        //2. Generate Movement Matrix
        w3.setWorkerView(t.generateMovementMatrix(w3));
        //3. Check GlobalEffect
        w3.setWorkerView(NoLevelUpCondition.getInstance().applyEffect(w3));
        //8. PassTurn
        t.passTurn();
        //CURRENT PLAYER - Player1
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),0,3);
        //8. PassTurn
        t.passTurn();
        //CURRENT PLAYER - Player2
        //1. Worker Selection Phase
        m.setSelectedWorker(w3);
        //2. Generate Movement Matrix
        w3.setWorkerView(t.generateMovementMatrix(w3));
        //3. Check GlobalEffect
        w3.setWorkerView(NoLevelUpCondition.getInstance().applyEffect(w3));
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),4,4);


        //Check if the workers have changed position
        assertFalse(Battlefield.getBattlefieldInstance().getCell(3,3).isWorkerPresent());
        assertEquals(battlefield.getCell(0, 3).getWorker(), w1);
        assertEquals(battlefield.getCell(4, 4).getWorker(), w3);
        //control workerView
        Cell[][] workerView =w3.getWorkerView();
        assertNotNull(workerView[4][4]);
        //control Towers Height
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(0, 4).getTower().getHeight());
        assertEquals(2, Battlefield.getBattlefieldInstance().getCell(3, 3).getTower().getHeight());
        assertEquals(3, Battlefield.getBattlefieldInstance().getCell(4, 4).getTower().getHeight());
        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}