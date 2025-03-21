

package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.basic.Color;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * GUIBuilder create GUI and, showing and hiding windows in phases of the game
 */
public class GUIBuilder extends Application {

    private Scene mainScene;
    private Stage mainStage;
    private ViewState state;
    private static GUIController controller;

    /*
    * Sets GUIController inside the application
    * */
    public static void setGUIController(GUIController GUIcontroller){
        controller = GUIcontroller;
    }

    /*
    * Gets GUIController
    * */
    public static GUIController getGUIController(){
        return controller;
    }


    /**
     * Create Window
     * @param stage Stage
     * @throws Exception exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        state = ViewState.CONNECTION;
        mainScene = new Scene(root);
        mainStage = stage;
        stage.setResizable(false);

        /* Handle Close event*/
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });


        LoginView view = new LoginView(root, this);

        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * Launch GUI
     * @param controller controller
     */
    public void launchGUI(GUIController controller){
        launch();
    }


    /**
     * Change view at the end of different phase.
     * @param view indicate in which picking card phase is the application. If player is god then he has to pick all the card
     *             and then waiting for card picking is his turn.
     * */
    public void changeView(Optional<ViewState> view) {
        Parent root = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        if(view.isPresent())
            state = view.get();

        switch(state){
            case CONNECTION:
                try {
                    root = FXMLLoader.load(getClass().getResource("/PlayerSettings.fxml"));
                    mainScene = new AddPlayerView(root, this);
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                mainStage.setResizable(false);
                break;
            case SELECT_GOD_CARD:
                try {

                    root = FXMLLoader.load(getClass().getResource("/CardPicker.fxml"));
                    // mainScene = new Scene(root);
                    mainScene = new SelectCardView(root, this, true);
                    state = ViewState.SELECT_CARD;
                }catch(IOException | ExecutionException | InterruptedException e){
                    System.out.println(e.getMessage());
                }
                mainStage.setResizable(false);
                break;
            case SELECT_CARD:
                try {
                    root = FXMLLoader.load(getClass().getResource("/CardPicker.fxml"));
                    // mainScene = new Scene(root);
                    mainScene = new SelectCardView(root, this,false);
                    state = ViewState.GAME;
                }catch(IOException | ExecutionException | InterruptedException e){
                    System.out.println(e.getMessage());
                }
                mainStage.setResizable(false);
                break;
            case GAME:
                try {
                    root = FXMLLoader.load(getClass().getResource("/Board.fxml"));
                    /// mainScene = new Scene(root);
                    mainScene = new BattlefieldView(root, this);
                    // state = ViewState.LOGIN;
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                break;
            default:
                root = new Pane();
        }

        mainStage.setScene(mainScene);
        mainStage.show();
        mainStage.setResizable(false);

    }

    /**
    * Shows Win Message to GUI
    * */
    public void showWin() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/WinMessage.fxml"));
            Scene actual = mainStage.getScene();
            ((StackPane) actual.lookup("#paneResult")).getChildren().clear();
            actual.lookup("#blurResult").setVisible(true);
            actual.lookup("#paneResult").setVisible(true);
            ((StackPane) actual.lookup("#paneResult")).getChildren().add(root);
            ((StackPane) actual.lookup("#paneResult")).setVisible(true);
            (root.lookup("#exitButton")).setOnMouseClicked(event->System.exit(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Shows Win Message to GUI
     * */
    public void showLose(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/LoseMessage.fxml"));
            Scene actual = mainStage.getScene();
            ((StackPane) actual.lookup("#paneResult")).getChildren().clear();
            actual.lookup("#blurResult").setVisible(true);
            actual.lookup("#paneResult").setVisible(true);
            ((StackPane) actual.lookup("#paneResult")).getChildren().add(root);
            ((StackPane) actual.lookup("#paneResult")).setVisible(true);
            (root.lookup("#exitButton")).setOnMouseClicked(event->System.exit(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
    * Gets controller
     * @return controller
    * */
    public GUIController GUIController(){
        return controller;
    }

    /**
     * Shows Cards in Game View
     * */
    public void showCards() {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("/CardDrawer.fxml"));
            Scene actual = mainStage.getScene();
            actual.lookup("#blurResult").setVisible(true);
            actual.lookup("#paneResult").setVisible(true);
            ((StackPane) actual.lookup("#paneResult")).getChildren().add(root);
            actual.lookup("#paneResult").setVisible(true);
            ListView<String> listView = ((ListView<String>) root.lookup("#cardsList"));
            listView.setCellFactory(param -> new BuildCell());
            listView.setItems(FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(GUIController.getController().getPlayers().stream().map(plauer->plauer.getCard()).collect(Collectors.toList()))));
            //Hide
            ((Button) actual.lookup("#closeButton")).setOnMouseClicked(event->{
                actual.lookup("#blurResult").setVisible(false);
                ((StackPane) actual.lookup("#paneResult")).getChildren().clear();
                ((StackPane) actual.lookup("#paneResult")).setVisible(false);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows error message on GUI
     * */
    public void showError() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ErrorMessage.fxml"));
            Scene actual = mainStage.getScene();
            ((StackPane) actual.lookup("#paneResult")).getChildren().clear();
            actual.lookup("#blurResult").setVisible(true);
            ((StackPane) actual.lookup("#paneResult")).getChildren().add(root);
            ((StackPane) actual.lookup("#paneResult")).setVisible(true);
            (root.lookup("#exitButton")).setOnMouseClicked(event->System.exit(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows error message on GUI while in Picking Card phase
     * */
    public void showErrorPicker(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/ErrorMessage.fxml"));
            Scene actual = mainStage.getScene();
           ((StackPane) actual.lookup("#resultPane")).getChildren().clear();
            actual.lookup("#blurResult").setVisible(true);
            ((StackPane) actual.lookup("#resultPane")).getChildren().add(root);
            ((StackPane) actual.lookup("#resultPane")).setVisible(true);
            (root.lookup("#exitButton")).setOnMouseClicked(event->System.exit(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
    * ListView template used for showing cards in game information
    * */
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

            /* Adding element to cell */
            Map<Color, String> colorWorker = new HashMap<>();
            colorWorker.put(Color.BLUE,getClass().getResource("/Images/Cards/BluePawnCard.png").toString());
            colorWorker.put(Color.BROWN, getClass().getResource("/Images/Cards/BrownPawnCard.png").toString());
            colorWorker.put(Color.GREY, getClass().getResource("/Images/Cards/GrayPawnCard.png").toString());

            super.updateItem(item, empty);
            if (empty || item == null) {
                imageView.setImage(null);

                setGraphic(null);
                setText(null);
            } else {
                try {
                    root = FXMLLoader.load(getClass().getResource("/CardTemplate.fxml"));
                    ((ImageView) root.lookup("#cardImage")).setImage(new Image(getClass().getResource("/Images/Cards/"+item+".png").toString()));
                    ((ImageView) root.lookup("#playerPawn")).setImage(new Image(colorWorker.get(GUIController.getController().getPlayers().stream().filter(el->el.getCard().equalsIgnoreCase(item)).map(el->el.getColor()).findFirst().get())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setGraphic(root);

            }
        }

    }
}
