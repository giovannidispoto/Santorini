package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
* Add Player Worker View
* */
public class AddPlayerView extends Scene {

    /**
     * Create AddPlayer Scene
     * @param root parent
     * @param builder GUIBuilder
     */
    public AddPlayerView(Parent root, GUIBuilder builder) {
        super(root);
        /*
        * Looking up element in fxml
        * */
        Button btn = (Button) root.lookup("#createButton");
        TextField nicknameField = (TextField) root.lookup("#nicknameField");
        RadioButton btn1 = (RadioButton) root.lookup("#twoPlayers");
        RadioButton btn2 = (RadioButton) root.lookup("#threePlayers");
        Label terminalLabel = (Label)  root.lookup("#terminalLabel");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        btn.setDisable(true);

        /*
         * Interrupt thread used for catching end of the game
         * */
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    try{
                        wait();
                    }catch( InterruptedException e){
                        //If the user if winner show relative view
                        Platform.runLater(()->terminalLabel.setTextFill(Color.web("#FC2A5D",1)));
                        Platform.runLater(()->terminalLabel.setText("Server Error!"));
                        nicknameField.setDisable(true);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });


        t.start();
        GUIController.getController().registerControllerThread(t);

        /*
        * Enable button when user insert username
        * */

        nicknameField.textProperty().addListener((observable, oldText, newText)->{
            if(!newText.isEmpty())
                btn.setDisable(false);
        });

        terminalLabel.setTextFill(Color.web("#FFFFFF",1));
        terminalLabel.setText("Setup Player Information...");

        /*Adding listener to button*/
        btn.setOnMouseClicked(e->{
           String nickname = nicknameField.getText().trim();
           boolean result;
           int number = (btn1.isSelected()) ? 2 : 3;

           /* Disable all */
            btn.setDisable(true);
            nicknameField.setDisable(true);
            btn1.setDisable(true);
            btn2.setDisable(true);

            /* Waiting for server response */
            Task<Void> wait = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    /* Disable input */
                    GUIController.getController().waitSetPickedCards();
                    return null;
                }
            };
            /*When server response, go on*/
            wait.setOnSucceeded( s ->{
                GUIController.getController().setGameState(GameState.LOBBY);

                //shutdown thread
                executorService.shutdown();
                if(GUIController.getController().getPlayerNickname().equals(GUIController.getController().getGodPlayer()))
                    builder.changeView(Optional.of(ViewState.SELECT_GOD_CARD));
                else
                    builder.changeView(Optional.of(ViewState.SELECT_CARD));
            });


            Task<Void> sendRequest = new Task<Void>() {
               @Override
               protected Void call() throws Exception {
                   try {
                       /*Send request to controller*/
                       GUIController.getController().setPlayerNickname(nickname);
                       GUIController.getController().addPlayerRequest(nickname, number);
                   } catch (SantoriniException santoriniException) {
                       System.out.println(santoriniException.getMessage());
                   }
                   return null;
               }
           };

            sendRequest.setOnSucceeded(event->{
                if(GUIController.getController().isFullLobby()){
                    btn.setDisable(true);
                    nicknameField.setDisable(true);
                    btn1.setDisable(true);
                    btn2.setDisable(true);
                    terminalLabel.setTextFill(Color.web("#FC2A5D",1));
                    terminalLabel.setText("The server is crowded...try later!️");
                } else if(GUIController.getController().getValidNick())
                {
                    terminalLabel.setTextFill(Color.web("#49CA3E",1));
                    terminalLabel.setText("Success! Wait for the match startup...");
                    executorService.execute(wait);
                }
                else{
                    //Request username
                    /* enable all */
                    btn.setDisable(false);
                    nicknameField.setDisable(false);
                    btn1.setDisable(false);
                    btn2.setDisable(false);
                    terminalLabel.setTextFill(Color.web("#FC2A5D",1));
                    terminalLabel.setText("Invalid Username...retry!");
                }
            });

            executorService.execute(sendRequest);
        });

    }
}
