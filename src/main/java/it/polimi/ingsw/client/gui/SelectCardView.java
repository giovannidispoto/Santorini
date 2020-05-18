package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.controller.GameState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCardView extends Scene {

    public SelectCardView(Parent root, GUIBuilder builder) throws IOException {
        super(root);
        Task<Void> wait = new Task<>() {
            @Override
            protected Void call() throws Exception {
                GUIController.getController().getDeckRequest();
                return null;
            }
        };
        /*When server response, go on*/
        wait.setOnSucceeded( s ->{
            ObservableList<String> deck = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getCardsDeck().getAllCards().stream().map(card -> card.getCardName()).collect(Collectors.toList())));

            ((ListView<String>) root.lookup("#listView")).setItems(deck);
            ((ListView<String>) root.lookup("#listView")).getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            ((ListView<String>) root.lookup("#listView")).setCellFactory(param->new BuildCell());

        });

        new Thread(wait).start();
    }

    private class BuildCell extends ListCell<String> {
        private ImageView imageView = new ImageView();

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                imageView.setImage(null);
                setGraphic(null);
                setText(null);
            } else {
                imageView.setImage(new Image(getClass().getResource("/Images/Cards/"+item+".png").toString()));
                imageView.setFitWidth(486*0.5);
                imageView.setFitHeight(858*0.5);
                setGraphic(imageView);
            }
        }
    }
}
