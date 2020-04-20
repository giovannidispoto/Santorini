package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.effects.global.GlobalWinCondition;
import it.polimi.ingsw.model.cards.effects.global.TowersCondition;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TowersConditionTest {
    final Player p1 = new Player("Player1", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Player2", LocalDate.now(), Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w3 = new Worker(p2);
    final DeckReader reader = new DeckReader();

    @Test
    void checkWinCondition() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader("src/Divinities.json"));
        p1.setPlayerCard(d.getDivinityCard("Atlas"));
        p2.setPlayerCard(d.getDivinityCard("Chronus"));
        players.add(p1);
        players.add(p2);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w3);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,3);
        w3.setWorkerPosition(3,3);
        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);
        //Add blocks
        //fullTower1
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(0,0).getTower().addBlock(Block.DOME);
        //fullTower2
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addBlock(Block.DOME);
        //fullTower3
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(0,2).getTower().addBlock(Block.DOME);
        //fullTower4
        Battlefield.getBattlefieldInstance().getCell(0,3).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,3).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,3).getTower().addBlock(Block.LEVEL_3);
        Battlefield.getBattlefieldInstance().getCell(0,3).getTower().addBlock(Block.DOME);
        //fullTower5
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_1);
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_2);
        Battlefield.getBattlefieldInstance().getCell(0,4).getTower().addBlock(Block.LEVEL_3);


        //Before Simulation Player2 choose Chronus
        List<GlobalWinCondition> globalWinConditions = new ArrayList<>();
        globalWinConditions.add(new TowersCondition(p2, m,5));

        //Simulation : CURRENT PLAYER - Player1
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Movement Matrix
        w1.setWorkerView(t.generateMovementMatrix(w1));
        //3. Check GlobalEffect...
        //4. Move()
        t.moveWorker(m.getSelectedWorker(),1,4);
        //5. CheckLocalWin...
        //6. BuildPhase...
        t.buildBlock(m.getSelectedWorker(),0,4);
        //7. CheckGlobalWin...
        globalWinConditions.get(0).checkWinCondition();

        //Control if p2 is the winner (turn is never passed) (with a debug variable in match)
        assertEquals(p2, m.winner);//debug

        //Clean battlefield for next tests
        battlefield.cleanField();
    }
}