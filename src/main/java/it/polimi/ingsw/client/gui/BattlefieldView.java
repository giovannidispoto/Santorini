package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.gui.component.AvailableCell;
import it.polimi.ingsw.client.gui.component.Dome;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.PlayerInterface;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BattlefieldView extends Scene {


    public BattlefieldView(Parent root, GUIBuilder guiBuilder) {
        super(root);
        ((Label) root.lookup("#phaseLabel")).setText("Waiting your turn");
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Task<Void> wait = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                GUIController.getController().waitSetWorkersPosition();
                GUIController.getController().getPlayersRequest();
                GUIController.getController().getBattlefieldRequest();
                return null;
            }
        };
        wait.setOnSucceeded(s->{
            System.out.println("HE");
            ((Label) root.lookup("#phaseLabel")).setText("Workers Placement");
            List<String> players = GUIController.getController().getPlayers().stream().map(PlayerInterface::getPlayerNickname).collect(Collectors.toList());
            for(String player : players){
                Label p = new Label(player);
                ((VBox) root.lookup("#playersVBox")).getChildren().add(p);
            }
            GridPane battlefieldGrid = ((GridPane) root.lookup("#battlefieldGrid"));

            for(int i = 0; i < BattlefieldClient.N_ROWS; i++) {
                for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                    if (!BattlefieldClient.getBattlefieldInstance().isCellOccupied(i, j)) {
                        battlefieldGrid.add(new AvailableCell(), i,j, 1,1);
                    }
                }
            }

        });

        new Thread(wait).start();

    }
}
