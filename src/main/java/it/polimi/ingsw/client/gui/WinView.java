package it.polimi.ingsw.client.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class WinView extends Scene {

    public WinView(Parent root, GUIBuilder builder){
        super(root);
        (root.lookup("#exitButton")).setOnMouseClicked(event->System.exit(0));
    }
}
