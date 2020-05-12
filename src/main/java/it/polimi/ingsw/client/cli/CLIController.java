package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.WorkerPositionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CLIController implements View {
    private final ClientController clientController;
    private final CLIBuilder commandLine;
    private final HashMap<Integer,String> moveMessages;

    public CLIController(String cliColor, ClientController clientController, Scanner consoleScanner) {
        this.clientController = clientController;
        this.moveMessages = new HashMap<>();
        this.commandLine = new CLIBuilder(cliColor, clientController, consoleScanner);
        moveMessages.put(0,"Wait ");}

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
            } while (!isYourTurn);

            //It's your Turn, choose type of turn
            clientController.setStartTurn(clientController.getPlayerNickname(), false);
            //Woke up by: SetStartTurnResponse

            do {
                //Select worker & get automatically his workerView
                commandLine.selectWorker(clientController);
                //Woke up by: WorkerViewUpdate
            }while(clientController.isInvalidWorkerView());
            //do all steps
            do {

                switch (clientController.getCurrentStep()){
                    case MOVE:
                        commandLine.moveWorker(clientController);
                        break;
                    case BUILD:
                        commandLine.buildBlock(clientController);
                        break;
                    case END:
                        break;
                    case MOVE_SPECIAL:
                        if(commandLine.askForSkip())
                            clientController.skipStepRequest();
                        else
                            commandLine.moveWorker(clientController);
                        break;
                    case BUILD_SPECIAL:
                        commandLine.buildBlock(clientController);
                        break;
                    case MOVE_UNTIL:
                        while (commandLine.askForRepeat())
                            commandLine.moveWorker(clientController);
                        break;
                    case REMOVE:
                        commandLine.removeBlock(clientController);
                        break;
                }

                //CurrentStep is Updated by Server after the step has been done
                if(clientController.getCurrentStep() != Step.END){
                    clientController.waitWorkerViewUpdate();
                    //Woke up by: WorkerViewUpdate
                }

            } while (Step.END != clientController.getCurrentStep());

        }while(GameState.FINISH != clientController.getGameState());

    }

    @Override
    public void printBattlefield() {
        commandLine.writeBattlefieldData(BattlefieldClient.getBattlefieldInstance());
        commandLine.renderBoard(moveMessages.get(0));
    }

    @Override
    public void callErrorMessage(String message) {
        commandLine.callError(message);
    }

    @Override
    public void callMatchResult(String result) {
       commandLine.callMatchResult(result);
    }
}
