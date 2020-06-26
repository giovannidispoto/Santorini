package it.polimi.ingsw.client.network;

import com.google.gson.JsonParseException;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.CommandFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ServerHandler get server response and send command to server,
 * manages the commands coming from the server and sending the request by the client
 */
public class ServerHandler{
    private final ClientController clientController;
    private final ServerThread serverThread;
    private Timer serverTimeout;

    /**
     * Create ServerHandler
     * @param clientController controller
     * @param serverThread ServerThread manage connection with Server
     */
    public ServerHandler(ClientController clientController, ServerThread serverThread) {
       this.clientController = clientController;
       this.serverThread = serverThread;
       clientController.registerHandler(this);
       this.serverTimeout = new Timer();
    }

    /**
     * Process command received from the server
     * @param m message
     */
    public void process(String m) {
        try {
            CommandFactory.from(m).execute(this.clientController);

        }catch(JsonParseException e) {
            clientController.setGameExceptionMessage(ExceptionMessages.jsonError, GameState.ERROR, true);
            clientController.loggerIO.severe("JSON-PARSING ERROR" + e.getMessage() + "\n");
        }
    }

    /**
     * Send request message to server
     * @param m message
     */
    public void request (String m){
        this.serverThread.send(m);
    }

    /**
     * Set the ping timer, wait 10s for a ping to arrive, if it does not arrive, notify the controller
     */
    public void setTimer(){
        serverTimeout = new Timer();
        serverTimeout.schedule(new TimerTask() {

            @Override
            public void run() {
                clientController.setGameExceptionMessage(ExceptionMessages.pingSocketError, GameState.ERROR, true);
                clientController.loggerIO.severe("NO-PING-ERROR\n");

                synchronized (WaitManager.waitPlayStepResponse){
                    /*If client is waiting for playStep response, wake*/
                    WaitManager.waitPlayStepResponse.notify();
                }

            }

        },10000);
    }

    /**
     * Clear the old ping timer and initialize another
     */
    public void resetServerTimeout(){
        serverTimeout.cancel();
        this.setTimer();
    }
}
