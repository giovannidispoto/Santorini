package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.controller.ClientController;
import org.junit.jupiter.api.Test;

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

        //flush objects
        clientController = null;
        clientSocketConnection = null;
    }

    @Test
    //Test correct messages and connection with server (need a listener server)
    void connectionServerTest(){
        ClientController clientController = new ClientController();
        ClientSocketConnection clientSocketConnection = new ClientSocketConnection( clientController);

        String serverName1 = "127.0.0.3";
        //check syntax
        assertTrue(clientSocketConnection.setServerName(serverName1));
        //Need Server UP listening on port 1337 with IP 127.0.0.3
        assertTrue(clientSocketConnection.startConnection());
        //On Server: respective JSON messages
        clientController.addNewPlayerRequest("Bill Gates","2019-05-06", Color.BLUE,"Apollo");
        clientController.setPlayerReadyRequest("Bill Gates");
        clientController.getWorkersIDRequest("Steve Jobs");
        clientController.getPlayersRequest();
        clientController.setInitialWorkerPositionRequest("Bill Gates", 0, 0,0);
        clientController.startTurnRequest("Steve Jobs", true);
        clientController.selectWorkerRequest("Steve Jobs", 2);
        clientController.playStepRequest(1,4);
        clientController.skipStepRequest();

        //flush objects
        clientController = null;
        clientSocketConnection = null;
    }


}