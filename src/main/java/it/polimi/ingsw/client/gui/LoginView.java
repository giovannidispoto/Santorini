package it.polimi.ingsw.client.gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Optional;

/* Login View */
public class LoginView {


    public LoginView(Parent root, GUIBuilder builder) {
            /* Looking up element on fxml */
            Button btn = (Button) root.lookup("#connectButton");
            Label terminalLabel = (Label) root.lookup("#terminalLabel");
            TextField socketPortField = (TextField) root.lookup("#socketPortField");
            TextField serverIPField = (TextField) root.lookup("#serverIPField");


            /* Adding listener to button*/
            btn.setOnMouseClicked(e->{
                /* Disable input */
                btn.setDisable(true);
                socketPortField.setDisable(true);
                serverIPField.setDisable(true);
                int socketPort = Integer.parseInt(socketPortField.getText().trim());
                String serverIP = serverIPField.getText().trim();

                /*Sending request to server*/
                boolean server = GUIController.getController().getSocketConnection().setServerName(serverIP);

                if(!server){
                    Platform.runLater(()-> terminalLabel.setText("Are you sure that server is right?"));
                    socketPortField.setDisable(false);
                    serverIPField.setDisable(false);
                    btn.setDisable(false);
                }else {
                    GUIController.getController().getSocketConnection().setServerPort(socketPort);
                    boolean result = GUIController.getController().getSocketConnection().startConnection();

                    if (result) {
                        builder.changeView(Optional.empty());
                    } else { //If there is a problem with connection, request another time
                        Platform.runLater(() -> terminalLabel.setText("Something went wrong"));
                        socketPortField.setDisable(false);
                        serverIPField.setDisable(false);
                        btn.setDisable(false);
                    }
                }
            });

    }

}
