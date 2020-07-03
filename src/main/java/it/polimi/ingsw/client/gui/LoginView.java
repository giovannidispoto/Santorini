package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.GameState;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Login View of the game
 */
public class LoginView {

    /**
     * Create Login View
     * @param root root
     * @param builder GUIBuilder
     */
    public LoginView(Parent root, GUIBuilder builder) {
        /* Looking up element on fxml */
        Button btn = (Button) root.lookup("#connectButton");
        Label terminalLabel = (Label) root.lookup("#terminalLabel");
        TextField socketPortField = (TextField) root.lookup("#socketPortField");
        TextField serverIPField = (TextField) root.lookup("#serverIPField");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //Disable button
        btn.setDisable(true);

        //Setup terminal label
        terminalLabel.setTextFill(Color.web("#FFFFFF",1));
        terminalLabel.setText("Setup Server Parameters...");

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
            int socketPort = 0;
            boolean invalidPort = false;
            /* Disable input */
            terminalLabel.setTextFill(Color.web("#FFFFFF",1));
            terminalLabel.setText("Handshaking with the Server...");
            btn.setDisable(true);
            socketPortField.setDisable(true);
            serverIPField.setDisable(true);
            try {
                socketPort = Integer.parseInt(socketPortField.getText().trim());
            }catch(NumberFormatException exception){
                invalidPort = true;
            }
            String serverIP = serverIPField.getText().trim();

            /*Sending request to server*/
            boolean server = GUIController.getController().getSocketConnection().setServerName(serverIP);

            if(!server || invalidPort){
                Platform.runLater(()-> terminalLabel.setText("This doesn't seem to be a Server..."));
                terminalLabel.setTextFill(Color.web("#FC2A5D",1));
                socketPortField.setDisable(false);
                serverIPField.setDisable(false);
                btn.setDisable(false);
            }else {
                /* Try server connection */
                int finalSocketPort = socketPort;
                Task<Void> connectTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        GUIController.getController().getSocketConnection().setServerPort(finalSocketPort);
                        boolean result = GUIController.getController().getSocketConnection().startConnection();
                        if (result) {//If connection is established correctly, change view
                            Platform.runLater(()->builder.changeView(Optional.empty()));
                        } else { //If there is a problem with connection, request another time
                            Platform.runLater(() -> terminalLabel.setText("Something went wrong...retry!"));
                            terminalLabel.setTextFill(Color.web("#FC2A5D",1));
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
