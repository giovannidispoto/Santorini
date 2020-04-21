package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.Controller;

import java.io.IOException;
import java.net.Socket;

/**
 * Client Class
 */
public class Client {

    private int port;
    private String serverName;
    private Socket clientSocket;
    private Controller controller;

    /**
     * Create new client at selected port.
     * Controller is one instance in all the application, so is created in main class and passed
     * @param port port
     * @param serverName Server IP
     * @param controller controller
     */
    public Client(int port, String serverName, Controller controller){
        this.port = port;
        this.serverName = serverName;
        this.controller = controller;
    }

    /**
     * Start Client
     * @throws IOException
     */
    public void startClient() throws IOException {
        System.out.println("Connecting to: " + serverName + " on port " + port);
        //open TCP port
        clientSocket = new Socket(serverName, port);

        System.out.println("Just connected to: " + clientSocket.getRemoteSocketAddress());
        // open input and output streams to read and write
        Thread thread = new Thread(new ServerThread(clientSocket, controller));
        thread.start();

    }

}