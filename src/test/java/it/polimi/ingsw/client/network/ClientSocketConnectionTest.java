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
        //On Server: respective JSON messages
        clientController.addPlayerRequest("Bill Gates", 2);
        /*
        System.out.println(clientController.waitLobbyReady());
        System.out.println("socket is closed:" + clientController.getSocketConnection().isSocketClosed());
        clientController.startNetwork();
        clientController.getSocketConnection().setServerName(serverName1);
        System.out.println(clientController.getSocketConnection().startConnection());
        System.out.println("socket is closed:" + clientController.getSocketConnection().isSocketClosed());
        clientController.addPlayerRequest("Bill", 2);
        clientController.getPlayersRequest();
         */

        /*
        clientController.getWorkersIDRequest("Steve Jobs");
        clientController.getPlayersRequest();
        clientController.setInitialWorkerPositionRequest("Bill Gates", 0, 0,0);
        clientController.startTurnRequest("Steve Jobs", true);
        clientController.selectWorkerRequest("Steve Jobs", 2);
        clientController.playStepRequest(1,4);
        clientController.skipStepRequest();

         */

        //Only if you send messages from server
        //while(true) {/*send message & debug*/}
    }


}