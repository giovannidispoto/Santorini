package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.network.data.dataInterfaces.WorkerPositionInterface;

import java.util.ArrayList;
import java.util.List;

public class CLIController implements View {
    private final ClientController clientController;
    private final CLIBuilder commandLine;

    public CLIController(String cliColor, ClientController clientController) {
        this.clientController = clientController;
        this.commandLine = new CLIBuilder(cliColor,clientController);
    }

    @Override
    public void startGame() throws SantoriniException {

        commandLine.setupConnection(clientController);
        //Connection with server is UP

        clientController.setGameState(GameState.LOBBY);

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

        clientController.setGameState(GameState.MATCH);

        //TODO:
        //----------------------------------------------------------------------------------    START MATCH
        //Wait Your Turn
        boolean isYou;
        do {
            do {
                clientController.waitActualPlayer();
                //Woke up by: ActualPlayer
                isYou = clientController.getActualPlayer().equals(clientController.getPlayerNickname());
                if (isYou) {
                    System.out.println("It's Your Turn");
                } else {
                    System.out.println("It's turn of: " + clientController.getActualPlayer());
                }
            } while (!isYou);

            //It's your Turn, choose type of turn
            clientController.setStartTurn(clientController.getPlayerNickname(), true);

            //do all steps
            do {
                clientController.getCurrentStep();
                //do something for every step, select worker
                clientController.selectWorkerRequest(clientController.getPlayerNickname(), clientController.getWorkersID().get(0));
                clientController.playStepRequest(2,2);

            } while (Step.END == clientController.getCurrentStep());

        }while(true);

    }

    @Override
    public void printBattlefield() {
        commandLine.writeBattlefieldData(BattlefieldClient.getBattlefieldInstance());
        commandLine.renderBoard(commandLine.transformMovesList());
    }
}
