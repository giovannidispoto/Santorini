package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 */
public class ClientThread implements Runnable {

    private final Socket socket;
    private ClientHandler clientHandler;
    private Controller controller;
    private PrintWriter out;

    /**
     *
     * @param socket
     * @param controller
     */
    public ClientThread(Socket socket,Controller controller) {
        this.socket = socket;
        this.controller = controller;
        this.clientHandler = new ClientHandler(controller, this);
       // this.clientHandler.setTimer();
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
                    clientHandler.process(line);
                    System.out.println("Received: "+line);
                }
            }
            //close streams and socket
            System.out.println("Closing sockets");
            in.close();
            out.close();
            socket.close();
        }catch (IOException | NoSuchElementException e){
           System.out.println(e.getMessage());
        }
    }

    /**
     * Send message to client
     * @param message
     */
    public void send(String message){
        out.println(message);
        out.flush();
    }
}
