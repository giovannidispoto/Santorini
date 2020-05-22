package it.polimi.ingsw.client.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Optional;

public class LoginView {


    public LoginView(Parent root, GUIBuilder builder) {

            Button btn = (Button) root.lookup("#connectButton");
            TextField socketPortField = (TextField) root.lookup("#socketPortField");
            TextField serverIPField = (TextField) root.lookup("#serverIPField");

            btn.setOnMouseClicked(e->{
                btn.setDisable(true);
                socketPortField.setDisable(true);
                serverIPField.setDisable(true);
                int socketPort = Integer.parseInt(socketPortField.getText().trim());
                String serverIP = serverIPField.getText().trim();
                GUIController.getController().getSocketConnection().setServerName(serverIP);
                GUIController.getController().getSocketConnection().setServerPort(socketPort);
                boolean result = GUIController.getController().getSocketConnection().startConnection();
                if(result){
                    System.out.println("Connection established");
                    builder.changeView(Optional.empty());
                }
            });

    }

}
