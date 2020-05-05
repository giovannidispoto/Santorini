package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.data.dataInterfaces.WorkerPositionInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClientSocketConnectionTest {

    @Test
    //Test ServerName Validator IP & Hostname
    void setServerNameTest() {
        ClientController clientController = new ClientController();
        ClientSocketConnection clientSocketConnection = new ClientSocketConnection( clientController);

        String serverName1 = "192.179.0.3";
        System.out.println(clientSocketConnection.setServerName(serverName1));
        assertTrue(clientSocketConnection.setServerName(serverName1));

        String serverName2 = "192.179.0.";
        System.out.println(clientSocketConnection.setServerName(serverName2));
        assertFalse(clientSocketConnection.setServerName(serverName2));

        String serverName3 = "google";
        System.out.println(clientSocketConnection.setServerName(serverName3));
        assertFalse(clientSocketConnection.setServerName(serverName3));

        String serverName4 = "google.it";
        System.out.println(clientSocketConnection.setServerName(serverName4));
        assertTrue(clientSocketConnection.setServerName(serverName4));
    }

    //@Test
    //Test correct messages and connection with server (need a listener server)
    void connectionServerTest(){
        ClientController clientController = new ClientController();
        clientController.initializeNetwork();

        String serverName1 = "127.0.0.3";
        //check syntax
        assertTrue(clientController.getSocketConnection().setServerName(serverName1));
        //Need Server UP listening on port 1337 with IP 127.0.0.3
        assertTrue(clientController.getSocketConnection().startConnection());

        //On Server: respective JSON messages (Attention blockingRequest)

        //-------------------------------------------------------------------------------------- MATCH CREATION
        //1
        clientController.addPlayerRequest("Bill",2);
        //2
        clientController.getDeckRequest();
        //3
        List<String> cards = new ArrayList<>();
        cards.add("ATHENA");
        cards.add("APOLLO");
        clientController.setPickedCardsRequest(cards);
        //4
        clientController.setPlayerCardRequest("ATHENA","Josh");
        //5
        clientController.getBattlefieldRequest();
        //6
        List<WorkerPositionInterface> workersPosition= new ArrayList<>();
        workersPosition.add(new WorkerPositionInterface(0,4,4));
        workersPosition.add(new WorkerPositionInterface(1,2,3));
        clientController.setWorkersPositionRequest("Bill",workersPosition);
        //-------------------------------------------------------------------------------------- START MATCH


    }

    //@Test
    void clientAlwaysWait(){
        ClientController clientController = new ClientController();
        clientController.initializeNetwork();

        String serverName1 = "127.0.0.3";
        //check syntax
        assertTrue(clientController.getSocketConnection().setServerName(serverName1));
        //Need Server UP listening on port 1337 with IP 127.0.0.3
        assertTrue(clientController.getSocketConnection().startConnection());

        //Only if you send messages from server
        clientController.getPlayersRequest();
    }


}