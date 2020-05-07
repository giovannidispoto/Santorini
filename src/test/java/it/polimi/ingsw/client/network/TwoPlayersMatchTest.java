package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.cli.CLIController;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.WorkerPositionInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TwoPlayersMatchTest {

    @Test
    void Player1Test(){
        ClientController clientController = new ClientController();
        clientController.setUserView(new CLIController("dark",clientController));
        clientController.initializeNetwork();

        //StartGame
        BattlefieldClient battlefieldClient = BattlefieldClient.getBattlefieldInstance();

        String serverName1 = "127.0.0.3";
        String player1 = "Bill";
        clientController.setPlayerNickname(player1);
        int serverPort = 1337;

        try {
            //Player1
            clientController.getSocketConnection().setServerName(serverName1);
            clientController.getSocketConnection().setServerPort(serverPort);
            clientController.getSocketConnection().startConnection();
            //Player1
            clientController.addPlayerRequest(player1, 2);
            clientController.setGameState(GameState.LOBBY);
            //Player1
            clientController.waitSetPickedCards();
            //Player1
            clientController.getDeckRequest();
            //Player1
            if (clientController.getGodPlayer().equals(player1)) {
                List<String> cards = new ArrayList<>();
                cards.add("ATHENA");
                cards.add("APOLLO");
                clientController.setPickedCardsRequest(cards);
            }
            //Player1
            clientController.waitSetPlayerCard();
            //Player1
            clientController.setPlayerCardRequest(player1, clientController.getGodCards().get(0));
            //Player1
            clientController.waitSetWorkersPosition();
            //Player1
            clientController.getPlayersRequest();
            //Player1
            clientController.getBattlefieldRequest();
            //Player1
            List<WorkerPositionInterface> workersPosition = new ArrayList<>();
            workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(0), 4, 4));
            workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(1), 4, 3));
            clientController.setWorkersPositionRequest(player1, workersPosition);
            clientController.setGameState(GameState.MATCH);
            //Player1
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
                clientController.setStartTurn(player1, true);
                System.out.println("My Step Is " + clientController.getCurrentStep());

                clientController.selectWorkerRequest(player1, 4,4);
                clientController.playStepRequest(3,4);
                //Test
                clientController.skipStepRequest();

            } while (true);
        }catch (SantoriniException e){
            System.out.println("Game Ended, error: " + e.getMessage());
        }

    }

    @Test
    void Player2Test(){
        ClientController clientController = new ClientController();
        clientController.setUserView(new CLIController("dark",clientController));
        clientController.initializeNetwork();

        //StartGame
        BattlefieldClient battlefieldClient = BattlefieldClient.getBattlefieldInstance();

        String serverName1 = "127.0.0.3";
        String player2 = "Marcus";
        clientController.setPlayerNickname(player2);
        int serverPort = 1337;

        try {
            //Player2
            clientController.getSocketConnection().setServerName(serverName1);
            clientController.getSocketConnection().setServerPort(serverPort);
            clientController.getSocketConnection().startConnection();
            //Player2
            clientController.addPlayerRequest(player2, 2);
            clientController.setGameState(GameState.LOBBY);
            //Player2
            clientController.waitSetPickedCards();
            //Player2
            clientController.getDeckRequest();
            //Player2
            if (clientController.getGodPlayer().equals(player2)) {
                List<String> cards = new ArrayList<>();
                cards.add("ATHENA");
                cards.add("APOLLO");
                clientController.setPickedCardsRequest(cards);
            }
            //Player2
            clientController.waitSetPlayerCard();
            //Player2
            clientController.setPlayerCardRequest(player2, clientController.getGodCards().get(0));
            //Player2
            clientController.waitSetWorkersPosition();
            //Player2
            clientController.getPlayersRequest();
            //Player2
            clientController.getBattlefieldRequest();
            //Player2
            List<WorkerPositionInterface> workersPosition = new ArrayList<>();
            workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(0), 0, 0));
            workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(1), 1, 0));
            clientController.setWorkersPositionRequest(player2, workersPosition);
            clientController.setGameState(GameState.MATCH);
            //Player2
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
                clientController.setStartTurn(player2, true);
                System.out.println("My Step Is " + clientController.getCurrentStep());

                clientController.selectWorkerRequest(player2, 0,0);
                clientController.playStepRequest(0,1);
                //Test
                clientController.skipStepRequest();

            } while (true);
        }catch (SantoriniException e){
            System.out.println("Game Ended, error: " + e.getMessage());
        }
    }

}
