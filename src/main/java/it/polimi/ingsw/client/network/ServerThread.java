package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 */
public class ServerThread implements Runnable {

    private final Socket socket;
    private final ServerHandler serverHandler;
    private ClientController clientController;
    private PrintWriter out;

    /**
     *Create new ServerThread to manage clientSocket
     * @param socket socket from Client initialisation
     * @param clientController clientController from Client initialisation
     */
    public ServerThread(Socket socket, ClientController clientController) {
        this.socket = socket;
        this.clientController = clientController;
        this.serverHandler = new ServerHandler(clientController, this);
    }

    /**
     *Run clientSocket on this thread, send messages to ServerHandler
     */
    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
            //read from and write to the connection until I receive "quit"
            String line;
            while ( !(null == (line = in.nextLine())) ) {
                if (line.equals("quit")) {
                    break;
                } else {
                    //line received
                    serverHandler.process(line);
                }
            }
            //close streams and socket
            in.close();
            this.out.close();
            this.socket.close();
            System.out.println("Socket Connection Closed");  //debug
        }catch (IOException  | NoSuchElementException e){
            e.printStackTrace();
            System.out.println("Input Scanner Exception");  //debug
            //TODO: debug
        }
    }

    /**
     * Send message to server
     * @param message to the server
     */
    public void send(String message){
        this.out.println(message);
        this.out.flush();
    }
}
