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

        commandLine.renderPlayersInfo(clientController);

        for(int workerID : clientController.getWorkersID()){
            workersPosition.add(commandLine.placeWorkers(clientController, workerID));
        }
        commandLine.writeBattlefieldData(BattlefieldClient.getBattlefieldInstance());
        commandLine.renderBoard("Wait");

        clientController.setWorkersPositionRequest(clientController.getPlayerNickname(), workersPosition);

        clientController.waitBattlefieldUpdate();
        //Woke up by: SetBattlefield
    }

    @Override
    public void printBattlefield() {
        commandLine.writeBattlefieldData(BattlefieldClient.getBattlefieldInstance());
        commandLine.renderBoard(commandLine.transformMovesList()); }
}
