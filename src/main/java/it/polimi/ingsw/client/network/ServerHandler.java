package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.network.actions.CommandFactory;

/**
 * ServerHandler get server response from socket and send command to server.
 * Every Client has his own ServerHandler
 */
public class ServerHandler{

    private final Controller controller;
    private final ServerThread thread;

    /**
     * Create ServerHandler
     * @param controller controller
     * @param thread thread
     */
    public ServerHandler(Controller controller, ServerThread thread){
       this.controller = controller;
       this.thread = thread;
    }

    /**
     * Process command received from the server
     * @param m message
     */
    public void process(String m){
        CommandFactory.from(m).execute(controller);
    }

    /**
     * Send message to server
     * @param m message
     */
    public void response(String m){
        this.thread.send(m);
    }
}
