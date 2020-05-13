package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.controller.SantoriniException;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;


public class AddPlayerView {
    public AddPlayerView(Parent root, GUIBuilder guiBuilder) {
        Button btn = (Button) root.lookup("#createButton");
        TextField nicknameField = (TextField) root.lookup("#nicknameField");
        RadioButton btn1 = (RadioButton) root.lookup("#twoPlayers");
        RadioButton btn2 = (RadioButton) root.lookup("#threePlayers");

        btn.setOnMouseClicked(e->{
           String nickname = nicknameField.getText().trim();
           boolean result;
           int number = (btn1.isSelected()) ? 2 : 3;
            try {
                GUIController.getController().addPlayerRequest(nickname, number);
            } catch (SantoriniException santoriniException) {
                santoriniException.printStackTrace();
            }
            System.out.println(nickname+ "add in game");
        });

    }
}
