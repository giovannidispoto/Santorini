package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.WaitManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Thread/Runnable that manages the connection with the server via socket, reads and writes to the socket
 */
public class ServerThread implements Runnable {

    private final Socket socket;
    private final ServerHandler serverHandler;
    private final ClientController clientController;
    private PrintWriter outStream;

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
     *  Run ClientSocket on this thread, send in-messages to ServerHandler
     */
    @Override
    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            this.outStream = new PrintWriter(socket.getOutputStream());
            //read from and write to the connection until I receive "quit"
            String line;
            while ( !(null == (line = in.nextLine())) ) {
                if (line.equals("quit")) {
                    break;
                } else {
                    //line received
                    serverHandler.process(line);
                    //log input messages
                    printLog(line);
                }
            }
            //close streams and socket
            in.close();
            this.outStream.close();
            this.socket.close();
            clientController.loggerIO.info("Socket Connection Closed, received quit\n");

        }catch (IOException  e1){
            clientController.setGameExceptionMessage(ExceptionMessages.IOSocketError, GameState.ERROR, true);
            clientController.loggerIO.severe("IO-EXCEPTION "+ e1.getMessage() + System.lineSeparator());
        }catch (NoSuchElementException e2){
            clientController.setGameExceptionMessage(ExceptionMessages.streamDownSocketError, GameState.ERROR, true);
            clientController.loggerIO.severe("NO-SOCKET-LINE-EXCEPTION "+ e2.getMessage() + System.lineSeparator());
        }
    }

    /**
     * Send message to server
     * @param message to the server
     */
    public void send(String message){
        this.outStream.println(message);
        this.outStream.flush();
    }

    /**
     * If the message is not a ping, it is printed in the logger
     * @param message to print (null not printed)
     */
    private void printLog(String message){
        if(null != message && !message.equals("{\"action\":\"ping\"}")){
            clientController.loggerIO.info(clientController.getPlayerNickname() + "Received: "+ message + System.lineSeparator());
        }
    }
}
