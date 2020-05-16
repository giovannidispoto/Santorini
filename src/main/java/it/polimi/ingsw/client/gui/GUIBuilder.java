package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.ClientController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIBuilder extends Application {

    private Scene mainScene;
    private Stage mainStage;


    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        mainScene = new Scene(root);
        mainStage = stage;
        LoginView view = new LoginView(root, this);
        stage.setResizable(false);
        stage.setScene(mainScene);
        stage.show();
    }


    public void launchGUI(){
        launch();
    }


    public void changeView() {

            try {
                Parent root = FXMLLoader.load(getClass().getResource("/AddPlayer.fxml"));
                mainScene = new Scene(root);
                mainStage.setScene(mainScene);
                mainStage.show();
                AddPlayerView view = new AddPlayerView(root, this);
            }catch(IOException e){
                System.out.println("Error");
            }
    }
}
