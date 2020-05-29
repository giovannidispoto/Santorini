package it.polimi.ingsw.client.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoseView extends Scene {

    public LoseView(Parent root, GUIBuilder builder){
        super(root);
        (root.lookup("#exitButton")).setOnMouseClicked(event->System.exit(0));
    }
}
