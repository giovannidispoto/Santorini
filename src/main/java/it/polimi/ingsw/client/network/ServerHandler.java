package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.network.commands.CommandFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ServerHandler get server response and send command to server
 */
public class ServerHandler{
    private final ClientController clientController;
    private final ServerThread thread;
    private Timer serverTimeout;

    /**
     * Create ServerHandler
     * @param clientController controller
     * @param thread ServerThread manage connection with Server
     */
    public ServerHandler(ClientController clientController, ServerThread thread) {
       this.clientController = clientController;
       this.thread = thread;
       clientController.registerHandler(this);
       this.serverTimeout = new Timer();
    }

    /**
     * Process command received from the server
     * @param m message
     */
    public void process(String m){
        CommandFactory.from(m).execute(this.clientController);
    }

    /**
     * Send request message to server
     * @param m message
     */
    public void request (String m){
        this.thread.send(m);
    }

    public void setTimer(){
        serverTimeout = new Timer();
        serverTimeout.schedule(new TimerTask() {

            @Override
            public void run() {
                clientController.setGameExceptionMessage(ExceptionMessages.streamDownSocketError);
                clientController.interruptNormalExecution();
            }

        },5000);
    }

    public void resetServerTimeout(){
        serverTimeout.cancel();
        this.setTimer();
    }
}
