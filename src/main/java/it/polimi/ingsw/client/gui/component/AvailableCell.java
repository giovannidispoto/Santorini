package it.polimi.ingsw.client.gui.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AvailableCell extends ImageView {

    public AvailableCell(){
        super.setImage(new Image(getClass().getResource("/Images/BoardElements/SelectedWorker.png").toString()));
        super.setFitWidth(80);
        super.setFitHeight(80);
    }
}
