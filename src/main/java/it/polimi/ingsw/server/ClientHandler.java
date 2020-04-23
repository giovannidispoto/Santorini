package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.actions.CommandFactory;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.CellInterface;
import it.polimi.ingsw.server.actions.data.CellMatrixResponse;

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
    public void update(CellInterface[][] cellInterfaces, Message message) {
        switch (message){
            case WORKERVIEW:
                response(new Gson().toJson(new BasicMessageResponse("workerViewUpdate", new CellMatrixResponse(cellInterfaces))));
                System.out.println("WorkerView Updated!");
                break;
            case BATTLEFIELD:
                response(new Gson().toJson(new BasicMessageResponse("battlefieldUpdate", new CellMatrixResponse(cellInterfaces))));
                System.out.println("Battlefield Updated!");
                break;
        }

    }
}
