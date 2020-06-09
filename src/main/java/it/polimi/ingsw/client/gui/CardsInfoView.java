package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardsInfoView extends Scene {

    public CardsInfoView(Parent parent) {
        super(parent);
        VBox cardBox = ((VBox) lookup("#cardBox"));
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Task<Void> wait = new Task<>() {
            @Override
            protected Void call() throws Exception {
                GUIController.getController().getDeckRequest();
                return null;
            }
        };

        wait.setOnSucceeded(event->{
            for(DivinityCard card: GUIController.getController().getCardsDeck().getAllCards()){
                try {
                    Parent view = FXMLLoader.load(getClass().getResource("/GodInformationTemplate.fxml"));
                    ((Label) view.lookup("#godName")).setText(card.getCardName());
                    ((Label) view.lookup("#godPower")).setText(card.getCardEffect());
                    cardBox.getChildren().add(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        });

        executorService.submit(wait);
    }
}
