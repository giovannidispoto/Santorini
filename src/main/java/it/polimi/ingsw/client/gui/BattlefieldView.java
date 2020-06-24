package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Block;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.gui.component.*;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.PlayerInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.WorkerPositionInterface;
import it.polimi.ingsw.model.Battlefield;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BattlefieldView extends Scene {

    private GridPane battlefieldGrid;
    private Label numberOfTower;
    private List<Integer> workersId;
    private List<WorkerPositionInterface> positions;
    private ExecutorService executor;
    private Task<Void> startTurn;
    private Task<Void> req;
    private Label actionLabel;
    private Parent root;
    private Map<Pair<Integer, Integer>, BattlefieldCell> battlefieldMap;
    private Map<Step, String> messageStep;
    private Button showCardButton;
    private Pair<Integer, Integer> selectedWorker = new Pair<>(-1,-1);
    private Button skipButton;


    public BattlefieldView(Parent root, GUIBuilder guiBuilder) {
        super(root);
        this.root = root;
        /* Pawn */
        Map<Color, String> colorWorker = new HashMap<>();
        colorWorker.put(Color.BLUE,getClass().getResource("/Images/Cards/BluePawnCard.png").toString());
        colorWorker.put(Color.BROWN, getClass().getResource("/Images/Cards/BrownPawnCard.png").toString());
        colorWorker.put(Color.GREY, getClass().getResource("/Images/Cards/GrayPawnCard.png").toString());

        actionLabel = (Label) root.lookup("#phaseLabel");
        numberOfTower = (Label) root.lookup("#fullTowersLabel");
        showCardButton = (Button) root.lookup("#cardsButton");
        skipButton = (Button) root.lookup("#skipButton");
        actionLabel.setText("Wait your turn");
        executor = Executors.newSingleThreadExecutor();
        //Battlefield Map
        battlefieldMap = new HashMap<>();

        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++){
                battlefieldMap.put(new Pair<>(i,j), new BattlefieldCell());
            }
        }

        battlefieldGrid = ((GridPane) root.lookup("#battlefieldGrid"));
        guiBuilder.GUIController().addBattlefield(this);

        /* Message Map */
        messageStep = new HashMap<>();
        messageStep.put(Step.MOVE, "Move");
        messageStep.put(Step.MOVE_SPECIAL, "Special Move");
        messageStep.put(Step.MOVE_UNTIL, "Looped Move");
        messageStep.put(Step.BUILD, "Build");
        messageStep.put(Step.BUILD_SPECIAL, "Special Build");
        messageStep.put(Step.REMOVE, "Remove");

        /*
         * Adding Workers to Battlefield Phase
         * */


        root.lookup("#skipButton").setDisable(true);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        battlefieldGrid = ((GridPane) root.lookup("#battlefieldGrid"));
        guiBuilder.GUIController().addBattlefield(this);



        Task<Void> wait = new Task<Void>() {
            @Override
            protected Void call()  {
                root.lookup("#blurResult").setVisible(true);
                showWait();
                try {
                    GUIController.getController().waitSetWorkersPosition();
                    GUIController.getController().getPlayersRequest();
                    GUIController.getController().getBattlefieldRequest();
                }catch(SantoriniException e){
                    System.out.println("Error");
                }
                return null;
            }
        };

        wait.setOnSucceeded(s->{
            root.lookup("#blurResult").setVisible(false);
            hideWait();
            positions = new ArrayList<>();
            workersId = new LinkedList<>(GUIController.getController().getWorkersID());
            ((Label) root.lookup("#phaseLabel")).setText("Workers Placement");
            List<PlayerInterface> players = GUIController.getController().getPlayers();
            for(PlayerInterface player : players){
                try {
                    Parent playerLable = FXMLLoader.load(getClass().getResource("/PlayerInformationTemplate.fxml"));
                    ((Label) playerLable.lookup("#playerNameLabel")).setText(player.getPlayerNickname());
                    ((Label) playerLable.lookup("#playerCardLabel")).setText(player.getCard());
                    ((ImageView) playerLable.lookup("#playerPawn")).
                            setImage(new Image(colorWorker.get(player.getColor())));
                    ((VBox) root.lookup("#playersVBox")).getChildren().add(playerLable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // populateBattlefield();
            populateBattlefieldMap();
            renderBattlefieldMap();
        });


        executor.execute(wait);


        /*
         * Starting Turn
         * Wait turn
         * */


        restartTurn();

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
                        if(GUIController.getController().getGameException().getMessage().equals(ExceptionMessages.winMessage)){
                            Platform.runLater(() -> guiBuilder.showWin());
                        }else if(GUIController.getController().getGameException().getMessage().equals(ExceptionMessages.loseMessage)){
                            System.out.println("You Lose");
                            Platform.runLater(() -> guiBuilder.showLose());
                        }else{
                            Platform.runLater(() -> guiBuilder.showError());
                        }
                        executor.shutdownNow();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });


        t.start();
        GUIController.getController().registerControllerThread(t);

        showCardButton.setOnMouseClicked(event->{
            guiBuilder.showCards();
        });

        //On skip clicked
        skipButton.setOnMouseClicked(event->{
            try {
                GUIController.getController().skipStepRequest();
                skipButton.setDisable(true);
                Platform.runLater(()->actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep())));
                if(GUIController.getController().getCurrentStep() != Step.END)
                    callRenderWorkerView();

                removeWorkerAvailableCell();

                if(GUIController.getController().getCurrentStep() == Step.END)
                    restartTurn();

            } catch (SantoriniException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    public void hideWait() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/WaitMessage.fxml"));
            Platform.runLater (()-> ((StackPane)  lookup("#paneResult")).getChildren().clear());
            Platform.runLater (()-> ((StackPane) lookup("#paneResult")).setVisible(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showWait() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/WaitMessage.fxml"));
            Platform.runLater (()-> ((StackPane) lookup("#paneResult")).getChildren().add(view));
            Platform.runLater (()-> ((StackPane) lookup("#paneResult")).setVisible(true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startBattlefieldPhase(){
        positions = new ArrayList<>();
        workersId = new LinkedList<>(GUIController.getController().getWorkersID());
        Platform.runLater(()-> ((Label) root.lookup("#phaseLabel")).setText("Workers Placement"));
        List<String> players = GUIController.getController().getPlayers().stream().map(PlayerInterface::getPlayerNickname).collect(Collectors.toList());
        for(String player : players){
            Label p = new Label(player);
            Platform.runLater(()-> ( (VBox) root.lookup("#playersVBox")).getChildren().add(p));
        }
        // populateBattlefield();
        populateBattlefieldMap();
        renderBattlefieldMap();
    }



    private void callRenderWorkerView(){
        req = new Task<Void>() {
            @Override
            protected Void call() {

                try {
                    GUIController.getController().waitWorkerViewUpdate();
                } catch (SantoriniException e) {
                    System.out.println(e.getMessage());
                }

                return null;
            }
        };

        req.setOnSucceeded(e->{
            populateWorkerViewMap();
            renderWorkerView();
        });

        executor.execute(req);

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
                        /* If number of workers remaming is 0, go on*/
                        if(workersId.size() == 0) {
                            GUIController.getController().setWorkersPositionRequest(positions);
                            GUIController.getController().setGameState(GameState.MATCH);
                            Platform.runLater(() -> actionLabel.setText("Wait your turn"));
                            cleanBattlefield();
                        }
                    });

                    Platform.runLater(()->battlefieldGrid.getChildren().add(ac));
                }else if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor() != null){
                    WorkerComponent worker = new WorkerComponent(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getWorkerColor());
                    battlefieldMap.get(new Pair<>(i, j)).setWc(worker);
                    GridPane.setRowIndex(worker, i);
                    GridPane.setColumnIndex(worker, j);
                    Platform.runLater(()->battlefieldGrid.getChildren().add(worker));
                }
            }
        }
    }

    private void restartTurn(){
        startTurn = new Task<>() {
            @Override
            protected Void call() {
                //default no basic turn

                Platform.runLater(() -> actionLabel.setText("Wait your turn"));

                do {
                    try {
                        GUIController.getController().waitActualPlayer();
                        GUIController.getController().getPlayersRequest();
                        GUIController.getController().getDeckRequest();
                    } catch (SantoriniException e) {
                        System.out.println("e.getMessage()");
                    }
                } while (!GUIController.getController().getActualPlayer().equals(GUIController.getController().getPlayerNickname()));

                resetBattlefieldMap();
                reloadBattlefield();
                return null;
            }
        };


        startTurn.setOnSucceeded(s -> {
            AtomicBoolean basicTurn = new AtomicBoolean(false);
            boolean requestInteraction = GUIController.getController().getCardsDeck().getDivinityCard(GUIController.getController().getPlayerCardName()).isChooseBasic();

            if(requestInteraction){
                //Request to user
                root.lookup("#godPowerBox").setVisible(true);
                ((Button) root.lookup("#acceptButton")).setOnMouseClicked(event->{
                    basicTurn.set(false);
                    root.lookup("#godPowerBox").setVisible(false);

                    Task<Void> innerTask1 = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                GUIController.getController().setStartTurn(basicTurn.get());
                            } catch (SantoriniException e) {
                                System.out.println(e.getMessage());
                            }
                            return null;
                        }
                    };

                    innerTask1.setOnSucceeded(e->{
                        Platform.runLater(() -> actionLabel.setText("Select Worker"));
                        enableClick();
                    });

                    executor.execute(innerTask1);
                });

                ((Button) root.lookup("#refuseButton")).setOnMouseClicked(event->{
                    basicTurn.set(true);
                    root.lookup("#godPowerBox").setVisible(false);

                    Task<Void> innerTask2 = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            try {
                                GUIController.getController().setStartTurn(basicTurn.get());
                            } catch (SantoriniException e) {
                                System.out.println(e.getMessage());
                            }
                            return null;
                        }
                    };

                    innerTask2.setOnSucceeded(e->{
                        Platform.runLater(() -> actionLabel.setText("Select Worker"));
                        enableClick();
                    });

                    executor.execute(innerTask2);
                });

            }else{

                Task<Void> innerTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            GUIController.getController().setStartTurn(false);
                        } catch (SantoriniException e) {
                            System.out.println(e.getMessage());
                        }
                        return null;
                    }
                };

                innerTask.setOnSucceeded(e ->{
                    Platform.runLater(() -> actionLabel.setText("Select Worker"));
                });

                executor.execute(innerTask);

            }



            for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
                for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                    for (Node node : battlefieldGrid.getChildren()) {
                        if (GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i) {
                            if (node instanceof WorkerComponent) {
                                if (BattlefieldClient.getBattlefieldInstance().getCell(i, j).getWorkerColor().equals(GUIController.getController().getPlayerColor())) {
                                    int finalI = i;
                                    int finalJ = j;
                                    //if request interaction, disable click on workers
                                    if(requestInteraction)
                                        node.setDisable(true);

                                    node.setOnMouseClicked(event -> {
                                        try {
                                            //clean old workerView
                                            removeWorkerAvailableCell();

                                            //enable other worker click
                                            enableClick();
                                            node.setDisable(true);

                                            selectedWorker = new Pair<>(finalI,finalJ);

                                            Platform.runLater(() -> actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep())));
                                            GUIController.getController().selectWorkerRequest(finalI, finalJ);

                                            populateWorkerViewMap();
                                            renderWorkerView();

                                            //disable button
                                            node.setDisable(true);

                                        } catch (SantoriniException e) {
                                            System.out.println(e.getMessage());
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

    private void enableClick(){
        for (int i = 0; i < BattlefieldClient.N_ROWS; i++) {
            for (int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                for (Node node : battlefieldGrid.getChildren()) {
                    if (GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i) {
                        if (node instanceof WorkerComponent) {
                            if (BattlefieldClient.getBattlefieldInstance().getCell(i, j).getWorkerColor().equals(GUIController.getController().getPlayerColor())) {
                                node.setDisable(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleWorkerViewselection(int i, int j){
        try {

            GUIController.getController().playStepRequest(i, j);

            if(GUIController.getController().getCurrentStep() == Step.MOVE){
                actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep()));
                skipButton.setDisable(true);
                callRenderWorkerView();
            }
            if(GUIController.getController().getCurrentStep() == Step.MOVE_SPECIAL){
                actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep()));
                skipButton.setDisable(false);
                callRenderWorkerView();
            }
            if(GUIController.getController().getCurrentStep() == Step.MOVE_UNTIL){
                actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep()));
                skipButton.setDisable(false);
                callRenderWorkerView();
            }
            if(GUIController.getController().getCurrentStep() == Step.BUILD_SPECIAL){
                actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep()));
                skipButton.setDisable(false);
                callRenderWorkerView();
            }
            if(GUIController.getController().getCurrentStep() == Step.BUILD) {
                actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep()));
                skipButton.setDisable(true);
                callRenderWorkerView();
            }
            if(GUIController.getController().getCurrentStep() == Step.REMOVE){
                //code for remove
                skipButton.setDisable(false);
                actionLabel.setText(messageStep.get(GUIController.getController().getCurrentStep()));
                callRenderWorkerView();
            }

            if(GUIController.getController().getCurrentStep() == Step.END) {
                skipButton.setDisable(true);
                //reloadBattlefield();
                //System.out.println("Reached end");
                restartTurn();
            }

            removeWorkerAvailableCell();
        } catch (SantoriniException e) {
            System.out.println(e.getMessage());
        }
    }

    private void populateWorkerViewMap(){

        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (GUIController.getController().getWorkerViewCell(i,j) && GUIController.getController().getWorkerView()[i][j]) {
                    WorkerViewCellAvailable ac = new WorkerViewCellAvailable();
                    battlefieldMap.get(new Pair<>(i,j)).setWvcl(ac);
                }else{
                    battlefieldMap.get(new Pair<>(i,j)).setWvcl(null);
                }
            }
        }
    }

    private void renderWorkerView(){
        for(int i = 0; i < BattlefieldClient.N_ROWS; i++){
            for(int j = 0; j < BattlefieldClient.N_COLUMNS; j++) {
                if (battlefieldMap.get(new Pair<>(i,j)).getWvcl() != null) {
                    WorkerViewCellAvailable ac = battlefieldMap.get(new Pair<>(i,j)).getWvcl();
                    GridPane.setRowIndex(ac, i);
                    GridPane.setColumnIndex(ac, j);
                    Platform.runLater(()->battlefieldGrid.getChildren().add(ac));
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
                            if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getLastBlock() == Block.DOME)
                                battlefieldMap.get(new Pair<>(i,j)).setDome(new Dome());
                            else
                                battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            break;
                        case 2:
                            battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getLastBlock() == Block.DOME)
                                battlefieldMap.get(new Pair<>(i,j)).setDome(new Dome());
                            else
                                battlefieldMap.get(new Pair<>(i,j)).setLvl2(new Level2());
                            break;
                        case 3:
                            battlefieldMap.get(new Pair<>(i,j)).setLvl1(new Level1());
                            battlefieldMap.get(new Pair<>(i,j)).setLvl2(new Level2());
                            if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getLastBlock() == Block.DOME)
                                battlefieldMap.get(new Pair<>(i,j)).setDome(new Dome());
                            else
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

        /*
         * Redraw all the battlefield
         * */
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
                            if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getLastBlock() == Block.DOME) {
                                dome = battlefieldMap.get(new Pair<>(i, j)).getDome();
                                GridPane.setRowIndex(dome,i);
                                GridPane.setColumnIndex(dome, j);
                                Platform.runLater(() ->  battlefieldGrid.getChildren().add(dome));
                            }else {
                                lvl1 = battlefieldMap.get(new Pair<>(i, j)).getLvl1();
                                GridPane.setRowIndex(lvl1,i);
                                GridPane.setColumnIndex(lvl1, j);
                                Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));
                            }
                            break;
                        case 2:
                            lvl1 = battlefieldMap.get(new Pair<>(i,j)).getLvl1();
                            GridPane.setRowIndex(lvl1,i);
                            GridPane.setColumnIndex(lvl1, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));

                            if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getLastBlock() == Block.DOME) {
                                dome = battlefieldMap.get(new Pair<>(i, j)).getDome();
                                GridPane.setRowIndex(dome,i);
                                GridPane.setColumnIndex(dome, j);
                                Platform.runLater(() ->  battlefieldGrid.getChildren().add(dome));
                            }else {
                                lvl2 = battlefieldMap.get(new Pair<>(i, j)).getLvl2();
                                GridPane.setRowIndex(lvl2,i);
                                GridPane.setColumnIndex(lvl2, j);
                                Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl2));
                            }
                            break;
                        case 3:
                            lvl1 = battlefieldMap.get(new Pair<>(i,j)).getLvl1();
                            lvl2 = battlefieldMap.get(new Pair<>(i,j)).getLvl2();
                            GridPane.setRowIndex(lvl1,i);
                            GridPane.setColumnIndex(lvl1, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl1));
                            GridPane.setRowIndex(lvl2,i);
                            GridPane.setColumnIndex(lvl2, j);
                            Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl2));

                            if(BattlefieldClient.getBattlefieldInstance().getCell(i,j).getLastBlock() == Block.DOME) {
                                dome = battlefieldMap.get(new Pair<>(i, j)).getDome();
                                GridPane.setRowIndex(dome,i);
                                GridPane.setColumnIndex(dome, j);
                                Platform.runLater(() ->  battlefieldGrid.getChildren().add(dome));
                            }else {
                                lvl3 = battlefieldMap.get(new Pair<>(i, j)).getLvl3();
                                GridPane.setRowIndex(lvl3,i);
                                GridPane.setColumnIndex(lvl3, j);
                                Platform.runLater(() ->  battlefieldGrid.getChildren().add(lvl3));
                            }
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

        //Set number of tower
        Platform.runLater(() -> numberOfTower.setText(BattlefieldClient.getBattlefieldInstance().countFullTowers()+""));
    }

}