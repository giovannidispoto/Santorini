package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.data.dataInterfaces.WorkerPositionInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIController implements View {
    private final ClientController clientController;
    private CLIBuilder commandLine;

    public CLIController(String cliColor, ClientController clientController) {
        this.clientController = clientController;
        this.commandLine = new CLIBuilder(cliColor,clientController);
    }

    @Override
    public void startGame() {

        commandLine.setupConnection(clientController);
        //Connection with server is UP
        clientController.waitSetPickedCards();
        //Woke up by: setPickedCards
        clientController.getDeckRequest();
        //Woke up by: getDeckResponse

        if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
            commandLine.pickCards(clientController);
        }
        else{
            commandLine.printGodPlayerActivity(clientController);
        }
        clientController.waitSetPlayerCard();
        //Woke up by: setPlayerCard

        //Player choose his card used in game
        commandLine.chooseCard(clientController);

        clientController.waitSetWorkersPosition();
        //Woke up by: SetWorkersPosition
        clientController.getPlayersRequest();
        clientController.getBattlefieldRequest();

        List<WorkerPositionInterface> workersPosition= new ArrayList<>();

        for(int workerID : clientController.getWorkersID()){
            //commandLine.stampBattlefield;
            System.out.println("Choose position for your workerID: " + workerID);
            workersPosition.add(placeWorker(clientController, workerID));
        }
        //commandLine.stampBattlefield;
        clientController.setWorkersPositionRequest(clientController.getPlayerNickname(), workersPosition);

        clientController.waitBattlefieldUpdate();
        //Woke up by: SetBattlefield
    }

    @Override
    public void printBattlefield() {

    }

    public WorkerPositionInterface placeWorker(ClientController clientController, int workerID){
        Scanner sc = new Scanner(System.in);
        boolean redoCondition;
        int x,y;
        do {
            System.out.print("Enter x: ");
            while (!sc.hasNextInt()) sc.next();
            x = sc.nextInt();
            System.out.print("Enter y: ");
            while (!sc.hasNextInt()) sc.next();
            y = sc.nextInt();
            if(BattlefieldClient.getBattlefieldInstance().isCellOccupied(x,y)){
                redoCondition = true;
                System.out.println("--Error Position already occupied--");
            }
            else {
                redoCondition = false;
                System.out.println("--position ok--");
            }
        }while(redoCondition);

        BattlefieldClient.getBattlefieldInstance().getCell(x,y).setPlayer(clientController.getPlayerNickname());
        BattlefieldClient.getBattlefieldInstance().getCell(x,y).setWorkerColor(clientController.getPlayerColor());
        return new WorkerPositionInterface(workerID, x, y);
    }
}
