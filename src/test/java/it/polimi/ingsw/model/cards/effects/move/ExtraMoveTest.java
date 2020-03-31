package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExtraMoveTest {

    final  Player p1 = new Player("Pippo", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Pluto", LocalDate.now() ,Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p2);

    @Test
    void moveWorker() {
        Battlefield b = Battlefield.getBattlefieldInstance();

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
        m.getSelectedWorker().setWorkerView(Battlefield.getBattlefieldInstance().getWorkerViewForMove(m.getSelectedWorker()));
        Turn t = m.generateTurn();

        t.moveWorker(m.getSelectedWorker(),1,1);

        //check that can't go back
        assertNull(m.getSelectedWorker().getWorkerView()[0][0]);

        t.moveWorker(m.getSelectedWorker(), 1,0);

        m.getSelectedWorker().setWorkerView(Battlefield.getBattlefieldInstance().getWorkerView(w1,(cell)->!cell.isWorkerPresent()));
        t.buildBlock(m.getSelectedWorker(), 1, 1);
        t.passTurn();

        assertFalse(Battlefield.getBattlefieldInstance().getCell(0,0).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(0,4).isWorkerPresent());
        assertFalse(Battlefield.getBattlefieldInstance().getCell(1,1).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,0).isWorkerPresent());
        assertTrue(Battlefield.getBattlefieldInstance().getCell(1,1).getTower().getHeight() == 1);

        assertThrows(RuntimeException.class, ()->m.setSelectedWorker(w1));

    }
}