package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattlefieldTest {

    final  Player p1 = new Player("Pippo", LocalDate.now(), Color.BLUE);
    final Player p2 = new Player("Pluto", LocalDate.now() ,Color.GREY);
    final Worker w1 = new Worker(p1);
    final Worker w2 = new Worker(p2);


    @Test
    void battleFieldCreation() {
        Battlefield b = Battlefield.getBattlefieldInstance();
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);
        w2.setWorkerPosition(0,4);

        Battlefield.getBattlefieldInstance().cleanField();
    }

    @Test
    void battleFieldChangeWorkerPosition(){
        Battlefield b = Battlefield.getBattlefieldInstance();

        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);
        w2.setWorkerPosition(0,4);

        assertTrue(b.getCell(0,0).getWorker().equals(w1));
        assertTrue(b.getCell(0,4).getWorker().equals(w2));

        w1.changeWorkerPosition(3,3);
        w2.changeWorkerPosition(4,4);

        assertTrue(!b.getCell(0,0).isWorkerPresent());
        assertTrue(!b.getCell(0,4).isWorkerPresent());
        assertTrue(b.getCell(3,3).getWorker().equals(w1));
        assertTrue(b.getCell(4,4).getWorker().equals(w2));
        Battlefield.getBattlefieldInstance().cleanField();
    }

    @Test
    void battleFieldWorkerView() {
        Battlefield b = Battlefield.getBattlefieldInstance();
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.setWorkerPosition(0,0);
        w2.setWorkerPosition(3,3);

        Cell[][] workerViewOne = b.getWorkerView(w1);
        Cell[][] workerViewTwo = b.getWorkerView(w2);
        int nOne = 0;
        int nTwo = 0;

        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (workerViewOne[i][j] != null) nOne++;
                if (workerViewTwo[i][j] != null) nTwo++;
            }
        }

        assertEquals(nOne, 3);
        assertEquals(nTwo, 8);

        w1.changeWorkerPosition(0,2);
        w2.changeWorkerPosition(0,4);


       workerViewOne = b.getWorkerView(w1);
       workerViewTwo = b.getWorkerView(w2);
       nOne = 0;
       nTwo = 0;

        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (workerViewOne[i][j] != null) nOne++;
                if (workerViewTwo[i][j] != null) nTwo++;
            }
        }

        assertEquals(nOne,  5);
        assertEquals(nTwo, 3);

        //Testing two worker near
        w1.changeWorkerPosition(0,0);
        w2.changeWorkerPosition(0,1);

        //checking worker view
        workerViewOne = b.getWorkerView(w1, (cell)->!cell.isWorkerPresent());
        workerViewTwo = b.getWorkerView(w2, (cell)->!cell.isWorkerPresent());
        nOne = 0;
        nTwo = 0;

        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (workerViewOne[i][j] != null) nOne++;
                if (workerViewTwo[i][j] != null) nTwo++;
            }
        }

        assertEquals(nOne,  2);
        assertEquals(nTwo, 4);
        Battlefield.getBattlefieldInstance().cleanField();
    }
}