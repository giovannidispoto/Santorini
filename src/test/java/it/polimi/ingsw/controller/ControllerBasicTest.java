package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Battlefield;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.model.parser.DeckReader;
import it.polimi.ingsw.server.ClientHandler;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControllerBasicTest {

    @Test
    void testController() throws IOException {
        Controller c = new Controller();
        //Create Match
        c.startMatch();

        //Adding Players
        c.addNewPlayer("Steve Jobs", LocalDate.now(), Color.GREY, "Apollo");
        c.addNewPlayer("Bill Gates", LocalDate.now(), Color.BLUE, "Athena");

        //Creating new workers for players
        c.addWorkers("Steve Jobs",new ClientHandler());
        c.addWorkers("Bill Gates", new ClientHandler());

        //Set First Player of the match
        c.setFirstPlayer("Steve Jobs");

        //Get workers of the players
        List<Integer> workers = c.getWorkersId("Steve Jobs");

        //Set initial position of the workers
        c.setInitialWorkerPosition("Steve Jobs", workers.get(0), 0,0);
        c.setInitialWorkerPosition("Steve Jobs", workers.get(1), 3,2);

        //Start basic turn
        c.startTurn(true, "Steve Jobs");
        //Select worker
        c.selectWorker("Steve Jobs", workers.get(0));
        //Move
        c.playStep(0,1);
        //Build
        c.playStep(1,1);
        //Pass turn
        c.passTurn();


        //ASSERTs : We expect a new position for the worker and a new block beside him
        assertEquals(Battlefield.getBattlefieldInstance().getCell(0, 1).getWorker(), c.getPlayers().get(0).getPlayerWorkers().get(0));
        assertNotEquals(Battlefield.getBattlefieldInstance().getCell(4, 1).getWorker(), c.getPlayers().get(0).getPlayerWorkers().get(0));
        assertEquals(Battlefield.getBattlefieldInstance().getCell(3, 2).getWorker(), c.getPlayers().get(0).getPlayerWorkers().get(1));
        assertEquals(1, Battlefield.getBattlefieldInstance().getCell(1, 1).getTower().getHeight());

       Battlefield.getBattlefieldInstance().cleanField();
    }
}