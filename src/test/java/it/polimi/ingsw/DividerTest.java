package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

class DividerTest {

    @org.junit.jupiter.api.Test
    void doDivision() {
        Divider d = new Divider();
        double r = d.doDivision(3,2);
        assertNotEquals(r,1.5);
    }
}