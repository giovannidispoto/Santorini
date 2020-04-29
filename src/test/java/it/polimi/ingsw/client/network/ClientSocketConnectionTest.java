package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
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
        clientController.startNetwork();

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
        cards.add("Athena");
        cards.add("Apollo");
        clientController.setPickedCardsRequest(cards);
        //4
        clientController.getCardsInGameRequest();
        //5
        clientController.setPlayerCardRequest("Athena");
        //6
        clientController.getWorkersIDRequest("Bill");
        //7
        clientController.getBattlefieldRequest();
        //8
        clientController.setInitialWorkerPositionRequest("Bill",0,4,4);
        //-------------------------------------------------------------------------------------- START MATCH


    }

    //@Test
    void clientAlwaysWait(){
        ClientController clientController = new ClientController();
        clientController.startNetwork();

        String serverName1 = "127.0.0.3";
        //check syntax
        assertTrue(clientController.getSocketConnection().setServerName(serverName1));
        //Need Server UP listening on port 1337 with IP 127.0.0.3
        assertTrue(clientController.getSocketConnection().startConnection());

        //Only if you send messages from server
        while(true) {/*send message & debug*/}
    }


}