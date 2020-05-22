package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.ClientController;
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

public class GUIBuilder extends Application {

    private Scene mainScene;
    private Stage mainStage;
    private ViewState state;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        state = ViewState.CONNECTION;
        mainScene = new Scene(root);
        mainStage = stage;
        mainStage.setResizable(false);
        LoginView view = new LoginView(root, this);
        stage.setScene(mainScene);
        stage.show();
    }

    public void launchGUI(){
        launch();
    }


    public void changeView() {
        Parent root = null;

        switch(state){
            case CONNECTION:
                try {
                    root = FXMLLoader.load(getClass().getResource("/AddPlayer.fxml"));
                    mainScene = new AddPlayerView(root, this);
                    state = ViewState.SELECT_CARD;
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
                mainStage.setResizable(false);
                break;
            case SELECT_CARD:
                try {
                    root = FXMLLoader.load(getClass().getResource("/SelectCard.fxml"));
                   // mainScene = new Scene(root);
                    mainScene = new SelectCardView(root, this);
                    state = ViewState.SELECT_CARD;
                }catch(IOException e){
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
}
