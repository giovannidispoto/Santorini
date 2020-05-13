package it.polimi.ingsw.client.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginView {
    private TextField socketPortField;
    private TextField serverIPField;
    private GUIBuilder builder;

    public LoginView(Parent root, GUIBuilder builder) {

            Button btn = (Button) root.lookup("#connectButton");
            socketPortField = (TextField) root.lookup("#socketPortField");
            serverIPField = (TextField) root.lookup("#serverIPField");
            this.builder = builder;
            btn.setOnMouseClicked(this::handle);

    }
    private void handle(MouseEvent mouseEvent){
        int socketPort = Integer.parseInt(socketPortField.getText().trim());
        String serverIP = serverIPField.getText().trim();
        GUIController.getController().getSocketConnection().setServerName(serverIP);
        GUIController.getController().getSocketConnection().setServerPort(socketPort);
        boolean result = GUIController.getController().getSocketConnection().startConnection();
        if(result){
            System.out.println("Connection established");
            builder.changeView();
        }
    }

}
