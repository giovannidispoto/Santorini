package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.parser.DeckReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.TestsStaticResources.absPathDivinitiesCardsDeck;
import static org.junit.jupiter.api.Assertions.*;

class ExtraBlockAboveTestFalse {

    final Player p1 = new Player("Steve Wozniak",Color.BLUE);
    final Worker w1 = new Worker(p1);
    final DeckReader reader = new DeckReader();
    //Testing extra block above set to false : DEMETER
    @Test
    void extraAboveFalse() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("DEMETER"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Steve Wozniak
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,1);
        //3. Build() again
        t.buildBlock(m.getSelectedWorker(),0,1);

        //ASSERTS
        assertEquals(1, battlefield.getCell(2, 1).getTower().getHeight());
        assertEquals(1, battlefield.getCell(0, 1).getTower().getHeight());

        battlefield.cleanField();
    }

    @Test
    void extraAboveFalseException() throws IOException {
        //Preliminary stuff
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        List<Player> players = new ArrayList<>();
        Deck d = reader.loadDeck(new FileReader(absPathDivinitiesCardsDeck));
        p1.setPlayerCard(d.getDivinityCard("DEMETER"));
        players.add(p1);
        List<Worker> workers = new ArrayList<>();
        workers.add(w1);
        battlefield.setWorkersInGame(workers);
        w1.setWorkerPosition(1,1);
        Match m = new Match(players);
        m.setCurrentPlayer(p1);

        //Simulation : CURRENT PLAYER - Steve Wozniak
        //0. Generate Turn
        Turn t = m.generateTurn(false);
        //1. Worker Selection Phase
        m.setSelectedWorker(w1);
        //2. Generate Building Matrix
        w1.setWorkerView(t.generateBuildingMatrix(w1));
        //3. Build()
        t.buildBlock(m.getSelectedWorker(),2,1);

        //ASSERTS
        Throwable expectedException = assertThrows(RuntimeException.class, () -> t.buildBlock(m.getSelectedWorker(),2,1));
        assertEquals("Unexpected Error!", expectedException.getMessage());

        battlefield.cleanField();
    }

}