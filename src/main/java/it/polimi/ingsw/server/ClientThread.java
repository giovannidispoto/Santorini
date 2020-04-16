package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {

    private final Socket socket;
    private ClientHandler clientHandler;

    public ClientThread(Socket socket) {
        this.socket = socket;
        clientHandler = new ClientHandler();
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            //read from and write to the connection until I receive "quit"
            while (true) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                } else {
                    clientHandler.process(line);
                    out.println("Received: " + line);
                    out.flush();
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
}
