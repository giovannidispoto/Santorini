package it.polimi.ingsw.client.gui.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Dome extends ImageView implements ViewCell{


    public Dome() {
        super.setFitWidth(80);
        super.setFitHeight(80);
        super.setImage(new Image(getClass().getResource("/Images/BoardElements/Dome.png").toString()));
    }
}
