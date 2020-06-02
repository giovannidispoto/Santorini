package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}