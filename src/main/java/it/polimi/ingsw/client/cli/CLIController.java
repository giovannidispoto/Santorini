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
        //----------------------------------------------------------------------------------    LOGGED IN LOBBY

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
        boolean isYourTurn;
        do {
            do {
                clientController.waitActualPlayer();
                //Woke up by: ActualPlayer
                isYourTurn = clientController.getActualPlayer().equals(clientController.getPlayerNickname());
                if (isYourTurn) {
                    System.out.println("It's Your Turn");
                } else {
                    System.out.println("It's turn of: " + clientController.getActualPlayer());
                }
            } while (!isYourTurn);

            //It's your Turn, choose type of turn
            clientController.setStartTurn(clientController.getPlayerNickname(), true);
            //Woke up by: SetStartTurnResponse

            //do all steps
            do {
                do {
                    //Select worker & get automatically his workerView
                    clientController.selectWorkerRequest(clientController.getPlayerNickname(), clientController.getWorkersID().get(0));
                    //Woke up by: WorkerViewUpdate
                }while(clientController.isInvalidWorkerView());
                //do something for every step
                switch (clientController.getCurrentStep()){
                    case MOVE:
                        clientController.playStepRequest(2,2);
                        break;
                    case BUILD:
                        break;
                    case END:
                        break;
                    case MOVE_SPECIAL:
                        break;
                    case BUILD_SPECIAL:
                        break;
                    case MOVE_UNTIL:
                        break;
                    case REMOVE:
                        break;
                }

            } while (Step.END == clientController.getCurrentStep());

        }while(GameState.FINISH == clientController.getGameState());

    }

    @Override
    public void printBattlefield() {
        commandLine.writeBattlefieldData(BattlefieldClient.getBattlefieldInstance());
        commandLine.renderBoard(commandLine.transformMovesList());
    }
}
