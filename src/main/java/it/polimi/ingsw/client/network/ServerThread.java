package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerThread implements Runnable {

    private final Socket socket;
    private final ServerHandler serverHandler;
    private final ClientController clientController;
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
     *  Run ClientSocket on this thread, send in-messages to ServerHandler
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
                    //log input messages
                    if(!line.equals("{\"action\":\"ping\"}")){
                        clientController.loggerIO.info(clientController.getPlayerNickname() + "Received: "+ line + "\n");
                    }
                }
            }
            //close streams and socket
            in.close();
            this.out.close();
            this.socket.close();
            clientController.loggerIO.info("Socket Connection Closed, received quit\n");

        }catch (IOException  e1){
            clientController.setGameState(GameState.ERROR);
            clientController.setGameExceptionMessage(ExceptionMessages.IOSocketError);
            clientController.interruptNormalExecution();
            clientController.loggerIO.severe("IO-EXCEPTION "+ e1.getMessage() + "\n");
        }catch (NoSuchElementException e2){
            clientController.setGameState(GameState.ERROR);
            clientController.setGameExceptionMessage(ExceptionMessages.streamDownSocketError);
            clientController.interruptNormalExecution();
            clientController.loggerIO.severe("NO-SOCKET-LINE-EXCEPTION "+ e2.getMessage() + "\n");
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
