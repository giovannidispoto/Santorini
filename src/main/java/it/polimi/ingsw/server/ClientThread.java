package it.polimi.ingsw.server;

import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;

import java.io.IOException;
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
    private PrintWriter out;
    private Scanner in;
    private boolean socketShutdown;

    /**
     * ClientThread manage socket connection with client
     * @param socket clientSocket
     * @param lobbyManager manage lobbies
     */
    public ClientThread(Socket socket, LobbyManager lobbyManager) {
        this.socket = socket;
        this.clientHandler = new ClientHandler(lobbyManager, this);
        this.socketShutdown = false;
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
            clientHandler.setTimer();
            //read from and write to the connection until I receive "quit"
            while (!socketShutdown) {
                String line = in.nextLine();
                if (socketShutdown) {
                    break;
                } else {
                    clientHandler.process(line);
                    if(!line.contains("pong"))
                         System.out.println("Received: " + line + " From:" + clientHandler.getLobbyManager().getPlayerNickName(clientHandler));
                }
            }
            //close streams and socket
            closeSocket();
        }catch (IOException | NoSuchElementException e){
            if(!socketShutdown)
                clientHandler.playerDisconnected();
        }
    }

    /**
     * Send message to client
     * @param message string json message
     */
    public void send(String message){
        out.println(message);
        out.flush();
    }

    private void closeSocket(){
        in.close();
        out.close();
        if(!socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Socket Closed");
    }

    public void setSocketShutdown() {
        this.socketShutdown = true;
        clientHandler = null;
    }
}
