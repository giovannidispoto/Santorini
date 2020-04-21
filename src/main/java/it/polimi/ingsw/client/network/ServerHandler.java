package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.controller.Controller;
import it.polimi.ingsw.client.network.actions.CommandFactory;

/**
 * ServerHandler get server response from socket and send command to server.
 * Every Client has his own ServerHandler
 */
public class ServerHandler{

    private Controller controller;
    private ServerThread thread;

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
     * Process command received from socket
     * @param m message
     */
    public void process(String m){
        //CommandFactory.from(m).execute(controller, this);
    }

    /**
     * Send message to client
     * @param m message
     */
    public void response(String m){
        this.thread.send(m);
    }
}
