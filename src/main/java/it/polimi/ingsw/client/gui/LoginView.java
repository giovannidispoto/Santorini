package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.GameState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/* Login View */
public class LoginView {


    public LoginView(Parent root, GUIBuilder builder) {
            /* Looking up element on fxml */
            Button btn = (Button) root.lookup("#connectButton");
            Label terminalLabel = (Label) root.lookup("#terminalLabel");
            TextField socketPortField = (TextField) root.lookup("#socketPortField");
            TextField serverIPField = (TextField) root.lookup("#serverIPField");
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            //Disable button
            btn.setDisable(true);

            AtomicBoolean emptyIP = new AtomicBoolean(true);
            AtomicBoolean emptyPort = new AtomicBoolean(true);

            /*
            * Activate button when user insert IP and port
            * */
            socketPortField.textProperty().addListener((observable,oldText, newText)->{
                emptyPort.set(newText.isEmpty());

                if(!emptyPort.get() && !emptyIP.get())
                    btn.setDisable(false);
                else
                    btn.setDisable(true);
            });

            serverIPField.textProperty().addListener((observable, oldText, newText)->{
                emptyIP.set(newText.isEmpty());

                if(!emptyPort.get() && !emptyIP.get())
                    btn.setDisable(false);
                else
                    btn.setDisable(true);
            });


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
                    /* Try server connection */
                    Task<Void> connectTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            GUIController.getController().getSocketConnection().setServerPort(socketPort);
                            boolean result = GUIController.getController().getSocketConnection().startConnection();
                            if (result) {//If connection is esablished correctly, change view
                                Platform.runLater(()->builder.changeView(Optional.empty()));
                            } else { //If there is a problem with connection, request another time
                                Platform.runLater(() -> terminalLabel.setText("Something went wrong"));
                                socketPortField.setDisable(false);
                                serverIPField.setDisable(false);
                                btn.setDisable(false);
                            }
                            return null;
                        }
                    };
                    executorService.submit(connectTask);
                }
            });

    }

}
