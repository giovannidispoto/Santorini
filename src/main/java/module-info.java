module it.polimi.ingsw {
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;
    requires gson;
    requires javafx.base;
    requires javafx.controls;
    requires java.sql;
    requires java.base;
    requires java.instrument;
    exports it.polimi.ingsw;
    opens it.polimi.ingsw.model.cards;
}