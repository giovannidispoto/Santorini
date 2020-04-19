package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Server Class
 */
public class Server {

    private int port;
    private ServerSocket serverSocket;
    private Controller controller;

    /**
     * Create new server at selcted port.
     * Controller is one instance in all the application, so is created in main class and passed
     * @param port port
     * @param controller controller
     */
    public Server(int port, Controller controller){
        this.port = port;
        this.controller = controller;
    }

    /**
     * Start Server
     * @throws IOException
     */
    public void startServer() throws IOException {
        //open TCP port
        serverSocket = new ServerSocket(port);
        System.out.println("Server socket ready on port: " + port);
        //wait for connection
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("Received client connection");
            // open input and output streams to read and write
            Thread thread = new Thread(new ClientThread(socket, controller));
            thread.start();
        }

    }

}