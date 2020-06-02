package it.polimi.ingsw.client.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SelectCardView extends Scene {
    private Map<String,Boolean> map;

    public SelectCardView(Parent root, GUIBuilder builder, boolean god) throws IOException, ExecutionException, InterruptedException {
        super(root);
        Task<Void> wait = null;
        Task<Void> wait1 = null;
        Task<Void> wait2 = null;


        ExecutorService executor = Executors.newFixedThreadPool(1);
        /* If player is god player start picking card  */
        if(god){
            ObservableList<String> deck =  FXCollections.emptyObservableList();

                map = new HashMap<>();

                ListView<String> listView = ((ListView<String>) root.lookup("#listView"));

                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        if (map.values().stream().filter(e -> e == true).count() < GUIController.getController().getCurrentLobbySize())
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
                        builder.changeView(Optional.empty());
                    });

            wait = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    GUIController.getController().getDeckRequest();
                    return null;
                }
            };

            wait.setOnSucceeded(s->{
                /* Show cards inside ListView*/
                listView.setItems(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getCardsDeck().getAllCards().stream().map(card->card.getCardName()).collect(Collectors.toList()))));
                for (int i = 0; i < listView.getItems().size(); i++)
                    map.put(listView.getItems().get(i), false);
                listView.setCellFactory(param -> new BuildCell());
            });

            executor.submit(wait);
        }else{
                map = new HashMap<>();

                ListView<String> listView = ((ListView<String>) root.lookup("#listView"));
                listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        if (map.values().stream().filter(e -> e == true).count() == 0)
                            map.put(t1, true);
                        //refresh list
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
                        GUIController.getController().setPlayerCardRequest(cards.get(0));
                        builder.changeView(Optional.empty());
                        //t2.start();
                    });

            wait1 = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    GUIController.getController().waitSetPlayerCard();
                    return null;
                }
            };

            wait1.setOnSucceeded(e->{
                listView.setItems(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getGodCards())));
                for (int i = 0; i < listView.getItems().size(); i++)
                    map.put(listView.getItems().get(i), false);
                listView.setCellFactory(param -> new BuildCell());
            });


            executor.submit(wait1);
        }
    }

    /* Create custom ListView for showing cards*/

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
