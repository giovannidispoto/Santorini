package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.Controller;

import java.io.IOException;
import java.net.Socket;

/**
 * Client Socket Connection Class
 */
public class ClientSocketConnection {
    private int port;
    private String serverName;
    private Socket clientSocket;
    private Controller controller;

    /**
     * Create new clientSocket
     * Controller is one instance in all the application, so is created in main class and passed
     * @param controller controller
     */
    public ClientSocketConnection(Controller controller){
        this.controller = controller;
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
        Thread thread = new Thread(new ServerThread(clientSocket, controller));
        thread.start();
        return true;
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