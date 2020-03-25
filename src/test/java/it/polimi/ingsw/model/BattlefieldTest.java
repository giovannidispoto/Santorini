package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattlefieldTest {

    @Test
    void battleFieldCreation() {
        Battlefield b = Battlefield.getBattelfieldInstance();
        Worker w1 = new Worker(new Player("Pippo", new Date(), Color.BLUE),Color.BLUE);
        Worker w2 = new Worker(new Player("Pluto", new Date(), Color.GREY),Color.GREY);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.changeWorkerPosition(0,0);
        w2.changeWorkerPosition(0,4);
    }

    @Test
    void battleFieldChangeWorkerPosition(){
        Battlefield b = Battlefield.getBattelfieldInstance();
        Worker w1 = new Worker(new Player("Pippo", new Date(), Color.BLUE),Color.BLUE);
        Worker w2 = new Worker(new Player("Pluto", new Date(), Color.GREY),Color.GREY);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.changeWorkerPosition(0,0);
        w2.changeWorkerPosition(0,4);

        assertTrue(b.getCell(0,0).getWorker().equals(w1));
        assertTrue(b.getCell(0,4).getWorker().equals(w2));

        w1.changeWorkerPosition(3,3);
        w2.changeWorkerPosition(4,4);

        assertTrue(!b.getCell(0,0).isWorkerPresent());
        assertTrue(!b.getCell(0,4).isWorkerPresent());
        assertTrue(b.getCell(3,3).getWorker().equals(w1));
        assertTrue(b.getCell(4,4).getWorker().equals(w2));

    }

    @Test
    void battleFieldWorkerView() {
        Battlefield b = Battlefield.getBattelfieldInstance();
        Worker w1 = new Worker(new Player("Pippo", new Date(), Color.BLUE),Color.BLUE);
        Worker w2 = new Worker(new Player("Pluto", new Date(), Color.GREY),Color.GREY);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        workers.add(w2);
        b.setWorkersInGame(workers);
        w1.changeWorkerPosition(0,0);
        w2.changeWorkerPosition(3,3);

        Cell[][] workerViewOne = b.getWorkerView(w1);
        Cell[][] workerViewTwo = b.getWorkerView(w2);
        int nOne = 0;
        int nTwo = 0;

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (workerViewOne[i][j] != null) nOne++;
                if (workerViewTwo[i][j] != null) nTwo++;
            }
        }

        assertEquals(nOne,  5);
        assertEquals(nTwo, 3);

    }
}