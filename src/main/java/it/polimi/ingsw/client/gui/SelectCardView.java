package it.polimi.ingsw.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SelectCardView {

    public SelectCardView(Parent root, GUIBuilder builder) throws IOException {
        ObservableList<String> cardsName = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getCardsDeck().getAllCards().stream().map(el -> el.getCardName()).collect(Collectors.toList())));
        ListView<String> cards = new ListView<>(cardsName);


    }
}
