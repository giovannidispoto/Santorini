package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;

import java.io.IOException;
import java.net.Socket;

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
        System.out.println("Connecting to: " + serverName + " on port " + port);
        //open TCP port
        try {
            clientSocket = new Socket(serverName, port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Just connected to: " + clientSocket.getRemoteSocketAddress());
        // open input and output streams to read and write
        Thread thread = new Thread(new ServerThread(clientSocket, clientController));
        thread.start();
        return clientSocket.isConnected();
    }

    /**
     * Check if the Socket with the server is closed
     * @return Boolean, true if socket is closed
     */
    public Boolean socketClosed(){
        return clientSocket.isClosed();
    }

    //Getter & Setter
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
}