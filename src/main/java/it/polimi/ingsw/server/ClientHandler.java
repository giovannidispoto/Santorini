package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.network.action.CommandFactory;
import it.polimi.ingsw.model.network.action.data.BattlefieldInterface;
import it.polimi.ingsw.model.network.action.data.CellInterface;

/**
 * ClientHandler execute commands from socket and send response to client.
 * Every Thread has his own ClientHandler, such as a Virtual Client
 */
public class ClientHandler implements Observer {

    private Controller controller;
    private ClientThread thread;

    /**
     * Create ClientHandler
     * @param controller controller
     * @param thread thread
     */
    public ClientHandler(Controller controller, ClientThread thread){
       this.controller = controller;
       this.thread = thread;
    }

    /**
     * Process command received from socket
     * @param m message
     */
    public void process(String m){
        CommandFactory.from(m).execute(controller, this);
    }

    /**
     * Send message to client
     * @param m message
     */
    public void response(String m){
        this.thread.send(m);
    }

    /**
     * Update received from subject observed
     * @param cellInterfaces matrix
     * @param message who update
     */
    @Override
    public void update(CellInterface[][] cellInterfaces, Match.Message message) {
        switch (message){
            case WORKERVIEW:
                response(new Gson().toJson(new BattlefieldInterface("workerViewUpdate", cellInterfaces)));
                break;
            case BATTLEFIELD:
                response(new Gson().toJson(new BattlefieldInterface("battlefieldUpdate", cellInterfaces)));
                System.out.println("Battlefield Updated!");
                break;
        }

    }
}
