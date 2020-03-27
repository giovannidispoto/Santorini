package it.polimi.ingsw.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TowerTest {


    @Test
    void getHeight() {
        Tower t = new Tower();
        Tower t1 = new Tower();
        Tower t2 = new Tower();

        t1.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_2);
        t2.addBlock(Block.LEVEL_3);
        t2.addBlock(Block.DOME);

        assertEquals(t.getHeight(), 0);
        assertEquals(t1.getHeight(), 1);
        assertEquals(t2.getHeight(), 4);

    }

    @Test
    void isCompleted() {
        Tower t = new Tower();
        Tower t1 = new Tower();
        Tower t2 = new Tower();

        t1.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_2);
        t2.addBlock(Block.LEVEL_3);
        t2.addBlock(Block.DOME);

        assertFalse(t.isCompleted());
        assertFalse(t1.isCompleted());
        assertTrue(t2.isCompleted());
    }

    @Test
    void removeBlock() {
        Tower t = new Tower();
        Tower t1 = new Tower();
        Tower t2 = new Tower();

        t1.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_2);
        t2.addBlock(Block.LEVEL_3);
        t2.addBlock(Block.DOME);

        /* Expected exception trying to remove block from a empty Tower */
        assertThrows(RuntimeException.class,  ()->{
            t.removeBlock(Block.DOME);
        });

        /* Expected exception trying to remove not the latest block of the tower */
        assertThrows(RuntimeException.class, ()->{
            t1.removeBlock(Block.DOME);
        });

        t2.removeBlock(Block.DOME);
        assertTrue(t2.getHeight() == 3);


    }

    @Test
    void removeLatestBlock() {

        Tower t = new Tower();
        Tower t1 = new Tower();
        Tower t2 = new Tower();

        t1.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.LEVEL_1);
        t2.addBlock(Block.DOME);
        t2.addBlock(Block.DOME);

        /*Expected error trying to remove latest block from empty tower*/
        assertThrows(RuntimeException.class, t::removeLatestBlock);
        assertThrows(RuntimeException.class, t2::removeLatestBlock);
        t1.removeLatestBlock();
        assertTrue(t1.getHeight() == 0);

    }
}