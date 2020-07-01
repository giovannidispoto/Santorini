package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.consoleUtilities.PrinterClass;
import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * ClientThread is the thread created for comunicate with client.
 * Every client connected has is own thread.
 */
public class ClientThread implements Runnable {

    private final Socket socket;
    private ClientHandler clientHandler;
    private PrintWriter out;
    private Scanner in;
    /**
     * Indicates the status required by the server, if true the socket must turn off without touching other parts of the program
     */
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
                    //Special treatment for ping messages, processed at the lowest possible level
                    if(line.matches(NetworkUtilities.pongRegex)){
                        replyPongMessage();
                    }else {
                        printDebugInfo(line);
                        clientHandler.process(line);
                    }

                }
            }
        }catch (IOException | NoSuchElementException e){
            if(isNotSocketShutdown())
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

    /**
     * Close the socket and input and output
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
        PrinterClass.getPrinterInstance().printMessage(PrinterClass.socketClosedMessage);
    }

    private void replyPongMessage(){
        clientHandler.cancelPingTimeoutTimer();
        //Set timer to default value
        clientHandler.setTimer(0);
    }

    private void printDebugInfo(String line){
        PrinterClass.getPrinterInstance().printSocketMessage(line, clientHandler.getNickName());
    }
}
