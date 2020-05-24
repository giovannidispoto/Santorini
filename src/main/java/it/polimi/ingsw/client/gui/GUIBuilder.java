

package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.model.Battlefield;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GUIBuilder extends Application {

    private Scene mainScene;
    private Stage mainStage;
    private ViewState state;
    private static GUIController controller;

    public static void setGUIController(GUIController GUIcontroller){
        controller = GUIcontroller;
    }

    public static GUIController getGUIController(){
        return controller;
    }


    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        state = ViewState.CONNECTION;
        mainScene = new Scene(root);
        mainStage = stage;

        LoginView view = new LoginView(root, this);

        stage.setScene(mainScene);
        stage.show();
    }


    public void launchGUI(GUIController controller){
        System.out.println(controller != null);
        launch();
    }


    public void changeView(Optional<ViewState> view) {
        Parent root = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future f;

        if(view.isPresent())
            state = view.get();
    /*
        switch(state){
            case SELECT_CARD:
                try {
                   f = executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                GUIController.getController().waitSetPlayerCard();
                            } catch (SantoriniException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                   f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SELECT_GOD_CARD:
               f =  executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GUIController.getController().getDeckRequest();
                        } catch (SantoriniException e) {
                            e.printStackTrace();
                        }
                    }
                });

                try {
                    f.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
        }*/

        switch(state){
            case CONNECTION:
                try {
                    root = FXMLLoader.load(getClass().getResource("/AddPlayer.fxml"));
                    mainScene = new AddPlayerView(root, this);
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                mainStage.setResizable(false);
                break;
            case SELECT_GOD_CARD:
                try {

                    root = FXMLLoader.load(getClass().getResource("/SelectCard.fxml"));
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
                    root = FXMLLoader.load(getClass().getResource("/SelectCard.fxml"));
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
                    root = FXMLLoader.load(getClass().getResource("/Battlefield.fxml"));
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

    }

    public GUIController GUIController(){
        return controller;
    }
}
