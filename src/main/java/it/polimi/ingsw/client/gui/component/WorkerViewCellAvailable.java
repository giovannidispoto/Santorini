package it.polimi.ingsw.client.gui.component;

import it.polimi.ingsw.client.clientModel.basic.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class WorkerViewCellAvailable extends ImageView {

    public WorkerViewCellAvailable() {
        super.setFitWidth(80);
        super.setFitHeight(80);
        super.setImage(new Image(getClass().getResource("/Images/BoardElements/AvailableCell.png").toString()));
    }
}