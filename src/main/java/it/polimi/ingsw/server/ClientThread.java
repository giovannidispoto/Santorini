package it.polimi.ingsw.server;

import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

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
     *  Continues the connection with the client, opening the input and output streams of the socket,
     *  also setting the Ping, after which the thread will wait to manage the client's inputs until it is blocked by the server
     */
    @Override
    public void run() {
        try {
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream());
            //Start Ping to Client after setting in & out
            clientHandler.setTimer(10);
            //read from and write to the connection until shutdown (from server)
            while (isNotSocketShutdown()) {
                String line = in.nextLine();
                if (isNotSocketShutdown()) {
                    if(line.contains("{\"action\":\"pong\"}")){
                        clientHandler.resetTimeout();
                        clientHandler.setTimer(0);
                    }else {
                        clientHandler.process(line);
                        if(printDebugInfo)
                            System.out.println("Received: " + line + " From:" + clientHandler.getLobbyManager().getPlayerNickName(clientHandler));
                    }

                }
            }
        }catch (IOException | NoSuchElementException e){
            if(isNotSocketShutdown() && !clientHandler.isMustStopExecution())
                clientHandler.playerDisconnected();
        }
        socketShutdown();
    }

    /**
     * Send message to client
     * @param message string json message
     */
    public void send(String message){
        out.println(message);
        out.flush();
    }

    /**
     * Close the socket input and output and close socket
     */
    private void closeSocket(){
        if(!socket.isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        in.close();
        out.close();
        System.out.println(ansiBLUE+"Socket Closed"+ansiRESET);
    }

    /**
     * Close the socket and delete all references, it also isolates ClientThread
     */
    public void socketShutdown() {
        if(isNotSocketShutdown()) {
            this.socketShutdown = true;
            closeSocket();
            clientHandler = null;
        }
    }

    /**
     * Check Socket Client Status
     * @return true if the socket has not been shutdown
     */
    public boolean isNotSocketShutdown() {
        return !socketShutdown;
    }
}
