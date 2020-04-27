package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Client Socket Connection Class
 */
public class ClientSocketConnection {
    private final int serverPort = 1337;
    private String serverName;
    private Socket clientSocket;
    private final ClientController clientController;

    /**
     * Create new clientSocket
     * ClientController is one instance in all the application, so is created in main class and passed
     * @param clientController controller
     */
    public ClientSocketConnection(ClientController clientController){
        this.clientController = clientController;
    }

    /**
     * Start Client Connection with socket to the server
     * @return Boolean, true if connection is established
     */
    public boolean startConnection(){
        //open TCP port
        try {
            clientSocket = new Socket(serverName, serverPort);
        } catch (IOException e) {
            return false;
        }
        // open input and output streams to read and write
        Thread thread = new Thread(new ServerThread(clientSocket, clientController));
        thread.start();
        return clientSocket.isConnected();
    }

    /**
     * Set serverName and validate
     * @param serverName String that represent hostname
     * @return true if syntax is correct
     */
    public boolean setServerName(String serverName) {
        try {
            InetAddress.getByName(serverName);
        } catch (UnknownHostException e) {
            //Problem, unknown host
            return false;
        }
        this.serverName = serverName;
        return true;
    }

    /**
     * Check if the Socket with the server is closed
     * @return Boolean, true if socket is closed
     */
    public boolean isSocketClosed(){
        return clientSocket.isClosed();
    }

    //Getter & Setter
    public String getServerName() {
        return serverName;
    }
    public int getServerPort() {
        return serverPort;
    }
}