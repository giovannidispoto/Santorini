package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.basic.Deck;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.controller.GameState;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SelectCardView extends Scene {
    private Map<String,Boolean> map;

    public SelectCardView(Parent root, GUIBuilder builder) throws IOException {
        super(root);
        Task<Void> wait;
        if(GUIController.getController().getGodPlayer().equals(GUIController.getController().getPlayerNickname())) {

            wait = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    GUIController.getController().getDeckRequest();
                    return null;
                }
            };
            /*When server response, go on*/
            wait.setOnSucceeded(s -> {
                ObservableList<String> deck = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getCardsDeck().getAllCards().stream().map(card -> card.getCardName()).collect(Collectors.toList())));
                map = new HashMap<>();

                for (int i = 0; i < deck.size(); i++)
                    map.put(deck.get(i), false);

                //  ObservableList<String> selectedCard = FXCollections.observableArrayList(cardS);
                ListView<String> listView = ((ListView<String>) root.lookup("#listView"));
                listView.setItems(deck);
                listView.setCellFactory(param -> new BuildCell());
                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        //listView.getSelectionModel().get
                        if (map.values().stream().filter(e -> e == true).count() < 2)
                            map.put(t1, true);
                        listView.refresh();
                    }
                });

                ((Button) root.lookup("#selectButton")).setOnMouseClicked(
                        e->{
                            List<String> cards = new LinkedList<>();
                            for(String card : map.keySet()){
                                if(map.get(card))
                                    cards.add(card);
                            }
                            GUIController.getController().setPickedCardsRequest(cards);

                            System.out.println("Event");
                        });


            });
        }else{
             wait = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    GUIController.getController().waitSetPlayerCard();
                    return null;
                }
            };
            /*When server response, go on*/
            wait.setOnSucceeded(s -> {
                System.out.println("Your turn");
                ObservableList<String> deck = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getGodCards()));
                map = new HashMap<>();

                for (int i = 0; i < deck.size(); i++)
                    map.put(deck.get(i), false);

                //  ObservableList<String> selectedCard = FXCollections.observableArrayList(cardS);
                ListView<String> listView = ((ListView<String>) root.lookup("#listView"));
                listView.setItems(deck);
                listView.setCellFactory(param -> new BuildCell());
                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        //listView.getSelectionModel().get
                        if (map.values().stream().filter(e -> e == true).count() < 1)
                            map.put(t1, true);
                        listView.refresh();
                    }
                });


            });
        }
        new Thread(wait).start();
    }

    private class BuildCell extends ListCell<String> {
        private ImageView imageView = new ImageView();
        private Parent root;

        protected void update(String item){
            super.updateItem(item, false);
            try {
                root = FXMLLoader.load(getClass().getResource("/CardTemplate.fxml"));
                ((ImageView) root.lookup("#cardImage")).setImage(new Image(getClass().getResource("/Images/Cards/"+item+".png").toString()));


            } catch (IOException e) {
                e.printStackTrace();
            }
            setGraphic(root);
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                imageView.setImage(null);

                setGraphic(null);
                setText(null);
            } else {
                try {
                    root = FXMLLoader.load(getClass().getResource("/CardTemplate.fxml"));
                    ((ImageView) root.lookup("#cardImage")).setImage(new Image(getClass().getResource("/Images/Cards/"+item+".png").toString()));

                    if(map.get(item) == true)
                        ((ImageView) root.lookup("#playerPawn")).setImage(new Image(getClass().getResource("/Images/Cards/SelectedCard.png").toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setGraphic(root);

            }
        }

    }
}
