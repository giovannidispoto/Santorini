package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.gui.component.AvailableCell;
import it.polimi.ingsw.client.gui.component.Dome;
import it.polimi.ingsw.client.gui.component.WorkerComponent;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.PlayerInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.WorkerPositionInterface;
import it.polimi.ingsw.model.Battlefield;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BattlefieldView extends Scene {

    private GridPane battlefieldGrid;
    private List<Integer> workersId;
    private List<WorkerPositionInterface> positions;


    public BattlefieldView(Parent root, GUIBuilder guiBuilder) {
        super(root);
        ((Label) root.lookup("#phaseLabel")).setText("Wait your turn");
        root.lookup("#skipButton").setDisable(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        battlefieldGrid = ((GridPane) root.lookup("#battlefieldGrid"));
        guiBuilder.GUIController().addBattlefield(this);
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
            positions = new ArrayList<>();
            workersId = new LinkedList<>(GUIController.getController().getWorkersID());
            ((Label) root.lookup("#phaseLabel")).setText("Workers Placement");
            List<String> players = GUIController.getController().getPlayers().stream().map(PlayerInterface::getPlayerNickname).collect(Collectors.toList());
            for(String player : players){
                Label p = new Label(player);
                ((VBox) root.lookup("#playersVBox")).getChildren().add(p);
            }
            populateBattlefield();
        });

       executor.submit(wait);

    }

    private void populateBattlefield(){
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (!BattlefieldClient.getBattlefieldInstance().isCellOccupied(i, j)) {
                    AvailableCell ac = new AvailableCell();
                    GridPane.setRowIndex(ac, i);
                    GridPane.setColumnIndex(ac,j);
                    ac.setOnMouseClicked(event->{
                        Node source = (Node) event.getSource();
                        Integer rowIndex = GridPane.getRowIndex(source);
                        Integer colIndex = GridPane.getColumnIndex(source);
                        System.out.println("Adding worker to: "+rowIndex+", "+colIndex);
                        if(BattlefieldClient.getBattlefieldInstance().isCellOccupied(rowIndex,colIndex) == false && workersId.size() > 0){
                            battlefieldGrid.getChildren().remove(event.getSource());
                            WorkerComponent worker = new WorkerComponent(GUIController.getController().getPlayerColor());
                            GridPane.setRowIndex(worker, rowIndex);
                            GridPane.setColumnIndex(worker, colIndex);
                            battlefieldGrid.getChildren().add(worker);
                            positions.add(new WorkerPositionInterface(workersId.get(0), rowIndex, colIndex));
                            workersId.remove(0);
                        }

                        if(workersId.size() == 0) {
                            GUIController.getController().setWorkersPositionRequest(GUIController.getController().getPlayerNickname(), positions);
                            GUIController.getController().setGameState(GameState.MATCH);
                            cleanBattlefield();
                        }
                    });

                    battlefieldGrid.getChildren().add(ac);
                }else if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor() != null){
                    WorkerComponent worker = new WorkerComponent(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor());
                    GridPane.setRowIndex(worker, i);
                    GridPane.setColumnIndex(worker, j);
                    battlefieldGrid.getChildren().add(worker);
                }
            }
        }
    }

    private void cleanBattlefield(){
        List<Node> pairs = new ArrayList<>();
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++){
                for (Node node : battlefieldGrid.getChildren()) {
                    if (GridPane.getColumnIndex(node) == i && GridPane.getRowIndex(node) == j) {
                        if(node instanceof AvailableCell)
                           pairs.add(node);
                    }
                }
            }
        }

        for(Node p : pairs){
            battlefieldGrid.getChildren().remove(p);
        }
    }

    public void reloadBattlefield(){
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++){
                if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getPlayer() != null) {
                    WorkerComponent wc = new WorkerComponent(BattlefieldClient.getBattlefieldInstance().getCell(i, j).getWorkerColor());
                    GridPane.setRowIndex(wc, i);
                    GridPane.setColumnIndex(wc, j);
                    Platform.runLater(() -> battlefieldGrid.getChildren().add(wc));
                }
            }
        }
    }
}
