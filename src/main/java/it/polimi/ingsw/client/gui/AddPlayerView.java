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


public class AddPlayerView extends Scene {

    public AddPlayerView(Parent root, GUIBuilder builder) {
        super(root);
        Button btn = (Button) root.lookup("#createButton");
        TextField nicknameField = (TextField) root.lookup("#nicknameField");
        RadioButton btn1 = (RadioButton) root.lookup("#twoPlayers");
        RadioButton btn2 = (RadioButton) root.lookup("#threePlayers");

        btn.setOnMouseClicked(e->{
           String nickname = nicknameField.getText().trim();
           boolean result;
           int number = (btn1.isSelected()) ? 2 : 3;
            try {
                GUIController.getController().setPlayerNickname(nickname);
                GUIController.getController().addPlayerRequest(nickname, number);
            } catch (SantoriniException santoriniException) {
                santoriniException.printStackTrace();
            }
            System.out.println(nickname+ " add in game");
            /* Waiting for the server */
            Task<Void> wait = new Task<>() {
                @Override
                protected Void call() throws Exception {
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
            });

            new Thread(wait).start();
        });

    }
}
