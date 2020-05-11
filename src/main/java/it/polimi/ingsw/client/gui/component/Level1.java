package it.polimi.ingsw.client.gui.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Level1 extends Rectangle {

    private final int width = 80;
    private final int height = 80;


    public Level1() throws FileNotFoundException {
        super.setWidth(width);
        super.setHeight(height);
        super.setFill(new ImagePattern(new Image(new FileInputStream("C:/Users/Giovanni/IdeaProjects/hellofx3/ASSETS/LVL1.png"))));
    }


}
