package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 */
public class ServerThread implements Runnable {

    private final Socket socket;
    private ServerHandler serverHandler;
    private Controller controller;
    private PrintWriter out;

    /**
     *
     * @param socket socket from Client initialisation
     * @param controller controller from Client initialisation
     */
    public ServerThread(Socket socket, Controller controller) {
        this.socket = socket;
        this.controller = controller;
        this.serverHandler = new ServerHandler(controller, this);
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
            //read from and write to the connection until I receive "quit"
            while (true) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    serverHandler.process(line);
                    System.out.println("Received: "+line);
                }
            }
            //close streams and socket
            System.out.println("Closing sockets");
            in.close();
            out.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Send message to server
     * @param message to the server
     */
    public void send(String message){
        out.println(message);
        out.flush();
    }
}
