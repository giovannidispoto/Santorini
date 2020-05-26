package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.gui.component.*;
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
import org.w3c.dom.events.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BattlefieldView extends Scene {

    private GridPane battlefieldGrid;
    private List<Integer> workersId;
    private List<WorkerPositionInterface> positions;
    private ExecutorService executor;
    private Task<Void> startTurn;
    private Task<Void> req;
    private Map<Pair<Integer, Integer>, BattlefieldCell> battlefieldMap;


    public BattlefieldView(Parent root, GUIBuilder guiBuilder) {
        super(root);

        ((Label) root.lookup("#phaseLabel")).setText("Waiting your turn");
        executor = Executors.newSingleThreadExecutor();
        battlefieldMap = new HashMap<>();

        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++){
                battlefieldMap.put(new Pair<>(i,j), new BattlefieldCell());
            }
        }

        battlefieldGrid = ((GridPane) root.lookup("#battlefieldGrid"));
        guiBuilder.GUIController().addBattlefield(this);

        /*
         * Adding Workers to Battlefield Phase
         * */


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
            // populateBattlefield();
            populateBattlefieldMap();
            renderBattlefieldMap();
        });

        executor.submit(wait);



        /*
         * Starting Turn
         * Waiting turn
         * */


        restartTurn();


    }

    private void callRenderWorkerView(){
        req = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
               // System.out.println("Waiting Worker View Update");
                GUIController.getController().waitWorkerViewUpdate();
               // System.out.println("Worker View Update");
                return null;
            }
        };

        req.setOnSucceeded(e->{
            System.out.println("Start Rendering");
            populateWorkerViewMap();
            renderWorkerView();
            System.out.println("Rendering");
        });

        executor.submit(req);
    }

    private void populateBattlefieldMap(){
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (!BattlefieldClient.getBattlefieldInstance().isCellOccupied(i, j)) {
                    battlefieldMap.get(new Pair<>(i,j)).setAc(new AvailableCell());
                }
            }
        }
    }

    private void renderBattlefieldMap(){
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (battlefieldMap.get(new Pair<>(i,j)).getAc() != null) {
                    AvailableCell ac = battlefieldMap.get(new Pair<>(i,j)).getAc();
                    GridPane.setRowIndex(ac, i);
                    GridPane.setColumnIndex(ac,j);
                    int finalI = i;
                    int finalJ = j;
                    ac.setOnMouseClicked(event->{
                        Node source = (Node) event.getSource();
                        Integer rowIndex = GridPane.getRowIndex(source);
                        Integer colIndex = GridPane.getColumnIndex(source);
                        System.out.println("Adding worker to: "+rowIndex+", "+colIndex);
                        if(BattlefieldClient.getBattlefieldInstance().isCellOccupied(rowIndex,colIndex) == false && workersId.size() > 0){
                            battlefieldGrid.getChildren().remove(event.getSource());
                            WorkerComponent worker = new WorkerComponent(GUIController.getController().getPlayerColor());
                            battlefieldMap.get(new Pair<>(finalI, finalJ)).setWc(worker);
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
                    battlefieldMap.get(new Pair<>(i, j)).setWc(worker);
                    GridPane.setRowIndex(worker, i);
                    GridPane.setColumnIndex(worker, j);
                    battlefieldGrid.getChildren().add(worker);
                }
            }
        }
    }

    private void restartTurn(){
        startTurn = new Task<>() {
            @Override
            protected Void call() throws Exception {
                //System.out.println("I'm listening");

                do {
                    GUIController.getController().waitActualPlayer();
                } while (!GUIController.getController().getActualPlayer().equals(GUIController.getController().getPlayerNickname()));
                //Starting basic turn
                resetBattlefieldMap();
                GUIController.getController().setStartTurn(GUIController.getController().getPlayerNickname(), true);
                GUIController.getController().getBattlefieldRequest();
                // System.out.println("Your Turn");
                return null;
            }
        };


        startTurn.setOnSucceeded(s -> {
            for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
                for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                    for (Node node : battlefieldGrid.getChildren()) {
                        if (GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i) {
                            if (node instanceof WorkerComponent) {
                                if (BattlefieldClient.getBattlefieldInstance().getCell(i, j).getWorkerColor().equals(GUIController.getController().getPlayerColor())) {
                                    // System.out.println(i+","+j+": "+BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor());
                                    int finalI = i;
                                    int finalJ = j;
                                    node.setOnMouseClicked(event -> {
                                        try {
                                           // executor.submit(req);
                                            GUIController.getController().selectWorkerRequest(GUIController.getController().getPlayerNickname(), finalI, finalJ);
                                             populateWorkerViewMap();
                                             renderWorkerView();

                                        } catch (SantoriniException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });
        executor.submit(startTurn);
    }

    private void handleWorkerViewselection(int i, int j){
        try {
            GUIController.getController().playStepRequest(i, j);
            removeWorkerAvailableCell();
            if(GUIController.getController().getCurrentStep() == Step.BUILD) {
                // renderOnUpdate();
                //populateWorkerViewMap();
                callRenderWorkerView();
               // reloadBattlefield();
                // renderWorkerView();
            }
            if(GUIController.getController().getCurrentStep() == Step.END) {
                removeWorkerAvailableCell();
                reloadBattlefield();
                restartTurn();
                System.out.println("Waiting new turn");
            }
        } catch (SantoriniException e) {
            System.out.println(e);
        }
    }

    private void populateWorkerViewMap(){

        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (GUIController.getController().getWorkerViewCell(i,j) && GUIController.getController().getWorkerView()[i][j]) {
                    System.out.println("("+i+","+j+")");
                    WorkerViewCellAvailable ac = new WorkerViewCellAvailable();
                    battlefieldMap.get(new Pair<>(i,j)).setWvcl(ac);
                }else{
                    battlefieldMap.get(new Pair<>(i,j)).setWvcl(null);
                }
            }
        }
    }

    private void renderWorkerView(){
        System.out.println("2");
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (battlefieldMap.get(new Pair<>(i,j)).getWvcl() != null) {
                    WorkerViewCellAvailable ac = battlefieldMap.get(new Pair<>(i,j)).getWvcl();
                    GridPane.setRowIndex(ac, i);
                    GridPane.setColumnIndex(ac, j);
                    battlefieldGrid.getChildren().add(ac);
                    int finalI = i;
                    int finalJ = j;
                    ac.setOnMouseClicked(event -> {
                        handleWorkerViewselection(finalI, finalJ);
                    });
                }
            }
        }
    }

    private void cleanBattlefield(){
        List<Node> pairs = new ArrayList<>();
        for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                battlefieldMap.get(new Pair<>(i, j)).setAc(null);
            }
        }

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

    private void removeWorkerAvailableCell(){
        for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                battlefieldMap.get(new Pair<>(i, j)).setWvcl(null);
            }
        }

        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++){
                for (Node node : battlefieldGrid.getChildren()) {
                    if (GridPane.getColumnIndex(node) == i && GridPane.getRowIndex(node) == j) {
                        if(node instanceof WorkerViewCellAvailable)
                            Platform.runLater(()->battlefieldGrid.getChildren().remove(node));
                    }
                }
            }
        }
    }

    public void resetBattlefieldMap(){
        for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                battlefieldMap.put(new Pair<>(i,j), new BattlefieldCell());
            }
        }
    }

    public void reloadBattlefield(){
        /* Remove Available Cell of Worker View from Battlefield*/
        Platform.runLater(()->battlefieldGrid.getChildren().clear());
        resetBattlefieldMap();


        if(GUIController.getController().getCurrentStep() == Step.BUILD){
            //callRenderWorkerView();
             removeWorkerAvailableCell();
             //populateWorkerViewMap();
        }




        for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor() != null)
                    battlefieldMap.get(new Pair<>(i, j)).setWc(new WorkerComponent(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor()));

                if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getHeight() > 0){
                    switch(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getHeight()){
                        case 1:
                            battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            break;
                        case 2:
                            battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            battlefieldMap.get(new Pair<>(i,j)).setLvl2(new Level2());
                            break;
                        case 3:
                            battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            battlefieldMap.get(new Pair<>(i,j)).setLvl2(new Level2());
                            battlefieldMap.get(new Pair<>(i,j)).setLvl3(new Level3());
                            break;
                        case 4:
                            battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            battlefieldMap.get(new Pair<>(i,j)).setLvl2(new Level2());
                            battlefieldMap.get(new Pair<>(i,j)).setLvl3(new Level3());
                            battlefieldMap.get(new Pair<>(i,j)).setDome(new Dome());
                            break;
                    }
                }
            }
        }


        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++){
                /**
                 * Render Towers
                 */
                if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getHeight() > 0){
                    //System.out.println("x:"+i+", y: "+j+": "+BattlefieldClient.getBattlefieldInstance().getCell(i,j).getHeight());
                    Level1 lvl1;
                    Level2 lvl2;
                    Level3 lvl3;
                    Dome dome;
                    switch(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getHeight()){
                        case 1:
                            lvl1 = battlefieldMap.get(new Pair<>(i,j)).getLvl1();
                            GridPane.setRowIndex(lvl1,i);
                            GridPane.setColumnIndex(lvl1, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));
                            break;
                        case 2:
                            lvl1 = battlefieldMap.get(new Pair<>(i,j)).getLvl1();
                            lvl2 = battlefieldMap.get(new Pair<>(i,j)).getLvl2();
                            GridPane.setRowIndex(lvl1,i);
                            GridPane.setColumnIndex(lvl1, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));
                            GridPane.setRowIndex(lvl2,i);
                            GridPane.setColumnIndex(lvl2, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl2));
                            break;
                        case 3:
                            lvl1 = battlefieldMap.get(new Pair<>(i,j)).getLvl1();
                            lvl2 = battlefieldMap.get(new Pair<>(i,j)).getLvl2();
                            lvl3 = battlefieldMap.get(new Pair<>(i,j)).getLvl3();
                            GridPane.setRowIndex(lvl1,i);
                            GridPane.setColumnIndex(lvl1, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));
                            GridPane.setRowIndex(lvl2,i);
                            GridPane.setColumnIndex(lvl2, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl2));
                            GridPane.setRowIndex(lvl3,i);
                            GridPane.setColumnIndex(lvl3, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl3));
                            break;
                        case 4:
                            lvl1 = battlefieldMap.get(new Pair<>(i,j)).getLvl1();
                            lvl2 = battlefieldMap.get(new Pair<>(i,j)).getLvl2();
                            lvl3 = battlefieldMap.get(new Pair<>(i,j)).getLvl3();
                            dome = battlefieldMap.get(new Pair<>(i,j)).getDome();
                            GridPane.setRowIndex(lvl1,i);
                            GridPane.setColumnIndex(lvl1, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));
                            GridPane.setRowIndex(lvl2,i);
                            GridPane.setColumnIndex(lvl2, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl2));
                            GridPane.setRowIndex(lvl3,i);
                            GridPane.setColumnIndex(lvl3, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl3));
                            GridPane.setRowIndex(dome,i);
                            GridPane.setColumnIndex(dome, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(dome));
                            break;
                    }
                }
                /*
                 * Check if there is a WorkerViewAvailableCell
                 * */

                if(battlefieldMap.get(new Pair<>(i,j)).getWvcl() != null){
                    System.out.println("OK");
                    WorkerViewCellAvailable ac = new WorkerViewCellAvailable();//battlefieldMap.get(new Pair<>(i,j)).getWvcl();
                    GridPane.setRowIndex(ac,i);
                    GridPane.setColumnIndex(ac, j);
                    Platform.runLater(() ->  battlefieldGrid.getChildren().add(ac));
                    int finalI = i;
                    int finalJ = j;
                    ac.setOnMouseClicked(event -> {
                        handleWorkerViewselection(finalI, finalJ);
                    });
                }

                if(battlefieldMap.get(new Pair<>(i,j)).getWc() != null){
                    WorkerComponent wc = battlefieldMap.get(new Pair<>(i,j)).getWc();
                    GridPane.setRowIndex(wc,i);
                    GridPane.setColumnIndex(wc, j);
                    Platform.runLater(() ->  battlefieldGrid.getChildren().add(wc));
                }

            }
        }




    }

}