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
    private int port;
    private String serverName;
    private Socket clientSocket;
    private ClientController clientController;

    /**
     * Create new clientSocket
     * ClientController is one instance in all the application, so is created in main class and passed
     * @param clientController controller
     */
    public ClientSocketConnection(ClientController clientController){
        this.clientController = clientController;
        clientController.setSocketConnection(this);
    }

    /**
     * Start Client Connection with socket to the server
     * @return Boolean, true if connection is established
     */
    public Boolean startConnection(){
        //open TCP port
        try {
            clientSocket = new Socket(serverName, port);
        } catch (IOException e) {
            return false;
        }
        // open input and output streams to read and write
        Thread thread = new Thread(new ServerThread(clientSocket, clientController));
        thread.start();
        return clientSocket.isConnected();
    }

    /**
     * Check if the Socket with the server is closed
     * @return Boolean, true if socket is closed
     */
    public Boolean isSocketClosed(){
        return clientSocket.isClosed();
    }

    //Getter & Setter
    public String getServerName() {
        return serverName;
    }

    /**
     * Set serverName and validate
     * @param serverName String that represent hostname
     * @return true if syntax is correct
     */
    public Boolean setServerName(String serverName) {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(serverName);
        } catch (UnknownHostException e) {
            //Problem, unknown host
            return false;
        }
        this.serverName = serverName;
        return true;
    }

    public int getPort() {
        return port;
    }

    //In Debug
    public Boolean setPort(int port) {
        if(port >= 0 && port <= 65535){
            this.port = port;
            return true;
        }
        return false;
    }
}