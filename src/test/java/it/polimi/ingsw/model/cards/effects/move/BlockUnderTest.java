package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockUnderTest {

    final  Player p1 = new Player("Steve Jobs", LocalDate.now(), Color.BLUE);
    final Worker w1 = new Worker(p1);

    @Test
    void buildUnderYourself() {
        Battlefield b = Battlefield.getBattlefieldInstance();

        List<Player> players = new ArrayList<>();
        players.add(p1);

        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);

        Match m = new Match(players,new ArrayList<>());
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Steve
        //0. Generate Turn
        Turn t = m.generateTurn();
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Worker View Matrix for movement
        m.getSelectedWorker().setWorkerView(Battlefield.getBattlefieldInstance().getWorkerViewForMove(m.getSelectedWorker()));
        //3. Check GlobalEffect...
        //4. Movement Phase
        t.moveWorker(m.getSelectedWorker(),1,1);
        //5. CheckLocalWin...
        t.checkLocalCondition(m.getSelectedWorker());
        //6. BuildPhase...
        t.buildBlock(m.getSelectedWorker(), 1, 1);
        //7. CheckGlobalWin...
        //8. PassTurn
       //t.passTurn();

        //ASSERTION
        //Check if the selected worker has changed his position
        assertFalse(Battlefield.getBattlefieldInstance().getCell(0,0).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,1).isWorkerPresent());
        //Check if the height of the tower under the selected worker has changed
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,1).getTower().getHeight()==1);
    }
}