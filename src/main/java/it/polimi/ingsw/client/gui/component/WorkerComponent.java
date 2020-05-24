package it.polimi.ingsw.client.gui.component;

import it.polimi.ingsw.client.clientModel.basic.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class WorkerComponent extends ImageView {

    public WorkerComponent(Color c) {
        Map<Color, String> colorWorker = new HashMap<>();
        colorWorker.put(Color.BLUE,getClass().getResource("/Images/BoardElements/Blue PAWN.png").toString());
        colorWorker.put(Color.BROWN, getClass().getResource("/Images/BoardElements/Brown Pawn.png").toString());
        colorWorker.put(Color.GREY, getClass().getResource("/Images/BoardElements/Grey PAWN.png").toString());

        super.setFitWidth(80);
        super.setFitHeight(80);
        super.setImage(new Image(colorWorker.get(c)));
    }
}
