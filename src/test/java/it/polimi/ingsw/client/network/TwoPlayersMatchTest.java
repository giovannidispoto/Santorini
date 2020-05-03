package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.WorkerPositionInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TwoPlayersMatchTest {

    @Test
    void Player1Test(){
        ClientController clientController = new ClientController();
        clientController.initializeNetwork();

        String serverName1 = "127.0.0.3";
        String player1 = "Bill";
        clientController.setPlayerNickname(player1);
        int serverPort = 1337;

        //Player1
        clientController.getSocketConnection().setServerName(serverName1);
        clientController.getSocketConnection().setServerPort(serverPort);
        clientController.getSocketConnection().startConnection();
        //Player1
        clientController.addPlayerRequest(player1,2);
        //Player1
        clientController.waitSetPickedCards();
        //Player1
        clientController.getDeckRequest();
        //Player1
        if(clientController.getGodPlayer().equals(player1)) {
            List<String> cards = new ArrayList<>();
            cards.add("ATHENA");
            cards.add("APOLLO");
            clientController.setPickedCardsRequest(cards);
        }
        //Player1
        clientController.waitSetPlayerCard();
        //Player1
        clientController.setPlayerCardRequest(player1,clientController.getGodCards().get(0));
        //Player1
        clientController.waitSetWorkersPosition();
        //Player1
        clientController.getPlayersRequest();
        //Player1
        clientController.getBattlefieldRequest();
        //Player1
        List<WorkerPositionInterface> workersPosition= new ArrayList<>();
        workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(0),4,4));
        workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(1),4,3));
        clientController.setWorkersPositionRequest(player1, workersPosition);
        //Player1
        clientController.waitBattlefieldUpdate();

    }

    @Test
    void Player2Test(){
        ClientController clientController = new ClientController();
        clientController.initializeNetwork();

        String serverName1 = "127.0.0.3";
        String player2 = "Marcus";
        clientController.setPlayerNickname(player2);
        int serverPort = 1337;

        //Player2
        clientController.getSocketConnection().setServerName(serverName1);
        clientController.getSocketConnection().setServerPort(serverPort);
        clientController.getSocketConnection().startConnection();
        //Player2
        clientController.addPlayerRequest(player2,2);
        //Player2
        clientController.waitSetPickedCards();
        //Player2
        clientController.getDeckRequest();
        //Player2
        if(clientController.getGodPlayer().equals(player2)) {
            List<String> cards = new ArrayList<>();
            cards.add("ATHENA");
            cards.add("APOLLO");
            clientController.setPickedCardsRequest(cards);
        }
        //Player2
        clientController.waitSetPlayerCard();
        //Player2
        clientController.setPlayerCardRequest(player2,clientController.getGodCards().get(0));
        //Player2
        clientController.waitSetWorkersPosition();
        //Player2
        clientController.getPlayersRequest();
        //Player2
        clientController.getBattlefieldRequest();
        //Player2
        List<WorkerPositionInterface> workersPosition= new ArrayList<>();
        workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(0),0,0));
        workersPosition.add(new WorkerPositionInterface(clientController.getWorkersID().get(1),1,0));
        clientController.setWorkersPositionRequest(player2, workersPosition);
        //Player2
        clientController.waitBattlefieldUpdate();
    }

}
