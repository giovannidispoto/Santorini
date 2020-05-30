package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* Add Player Worker View
* */
public class AddPlayerView extends Scene {

    public AddPlayerView(Parent root, GUIBuilder builder) {
        super(root);
        /*
        * Looking up element in fxml
        * */
        Button btn = (Button) root.lookup("#createButton");
        TextField nicknameField = (TextField) root.lookup("#nicknameField");
        RadioButton btn1 = (RadioButton) root.lookup("#twoPlayers");
        RadioButton btn2 = (RadioButton) root.lookup("#threePlayers");
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        /*Adding listener to button*/
        btn.setOnMouseClicked(e->{
           String nickname = nicknameField.getText().trim();
           boolean result;
           int number = (btn1.isSelected()) ? 2 : 3;
            try {
                /*Send request to controller*/
                GUIController.getController().setPlayerNickname(nickname);
                GUIController.getController().addPlayerRequest(nickname, number);
            } catch (SantoriniException santoriniException) {
                System.out.println(santoriniException.getMessage());
            }

            /* Waiting for server response */
            Task<Void> wait = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    /* Disable input */
                    btn.setDisable(true);
                    nicknameField.setDisable(true);
                    btn1.setDisable(true);
                    btn2.setDisable(true);
                    GUIController.getController().waitSetPickedCards();
                    return null;
                }
            };
            /*When server response, go on*/
            wait.setOnSucceeded( s ->{
                GUIController.getController().setGameState(GameState.LOBBY);
                if(GUIController.getController().getPlayerNickname().equals(GUIController.getController().getGodPlayer()))
                    builder.changeView(Optional.of(ViewState.SELECT_GOD_CARD));
                else
                    builder.changeView(Optional.of(ViewState.SELECT_CARD));

                //shutdown thread
                executorService.shutdown();
            });


            executorService.execute(wait);
        });

    }
}
