package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.ClientHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerCompleteTurn {

    @Test
    void completeTurnWithNoLevelUpCondition() throws IOException {

        //Adding some towers
        Battlefield.getBattlefieldInstance().getCell(0,1).getTower().addNextBlock();
        Battlefield.getBattlefieldInstance().getCell(1,2).getTower().addNextBlock();

        Controller c = new Controller();

        //Adding Players
        c.addNewPlayer("Steve Jobs", LocalDate.now(), Color.GREY, "Athena");
        c.addNewPlayer("Bill Gates", LocalDate.now(), Color.BLUE, "Prometheus");

        //Create Match
        c.startMatch();

        //Creating new workers for players
        c.addWorkers("Steve Jobs",new ClientHandler(null, null));
        c.addWorkers("Bill Gates", new ClientHandler(null, null));


        //Set First Player of the match
        c.setFirstPlayer("Steve Jobs");


        //Get workers of the players
        List<Integer> workers = c.getWorkersId("Steve Jobs");

        //Set initial position of the workers
        c.setInitialWorkerPosition("Steve Jobs", workers.get(0), 0,0);
        c.setInitialWorkerPosition("Steve Jobs", workers.get(1), 3,2);

        List<Integer> workers2 = c.getWorkersId("Bill Gates");
        //Set initial position for second player
        c.setInitialWorkerPosition("Bill Gates", workers2.get(0),2,2);
        c.setInitialWorkerPosition("Bill Gates", workers2.get(1), 4,4);

        //Start turn with effect
        c.startTurn(false, "Steve Jobs");
        //Select worker
        c.selectWorker("Steve Jobs", workers.get(0));
        //Move
        c.playStep(0,1);
        //Build
        c.playStep(1,1);
        //Pass turn


        //ASSERTs : We expect a new position for the worker and a new block beside him
        assertEquals(Battlefield.getBattlefieldInstance().getCell(0, 1).getWorker(), c.getPlayers().get(0).getPlayerWorkers().get(0));
        assertEquals(Battlefield.getBattlefieldInstance().getCell(0, 1).getTower().getHeight(),1);
        assertNotEquals(Battlefield.getBattlefieldInstance().getCell(0, 0).getWorker(), c.getPlayers().get(0).getPlayerWorkers().get(0));
        assertEquals(Battlefield.getBattlefieldInstance().getCell(3, 2).getWorker(), c.getPlayers().get(0).getPlayerWorkers().get(1));
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(1, 1).getTower().getHeight());


        //Now testing that second player can't level up
        c.startTurn(false, "Bill Gates");

        c.selectWorker("Bill Gates", workers2.get(0));


        //expecting build
        c.playStep(2,3);

        assertNotEquals(Battlefield.getBattlefieldInstance().getCell(2, 3).getWorker(), c.getPlayers().get(1).getPlayerWorkers().get(0));
        assertEquals(Battlefield.getBattlefieldInstance().getCell(2, 3).getTower().getHeight(), 1);
        assertEquals(Battlefield.getBattlefieldInstance().getCell(2, 2).getWorker(), c.getPlayers().get(1).getPlayerWorkers().get(0));
        assertNull(c.getPlayers().get(1).getPlayerWorkers().get(0).getWorkerView()[2][3]);

        //Trying to step up
        Throwable expectedException = assertThrows(RuntimeException.class, () -> c.playStep(2,3)); // -> Expect error

        //move
        c.playStep(3,3);

        //another build
        c.playStep(3,4);

        //Final check
        assertEquals(Battlefield.getBattlefieldInstance().getCell(3, 3).getWorker(), c.getPlayers().get(1).getPlayerWorkers().get(0));
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(3, 4).getTower().getHeight());


        Battlefield.getBattlefieldInstance().cleanField();
    }
}
