package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.actions.CommandFactory;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.CellInterface;
import it.polimi.ingsw.server.actions.data.CellMatrixResponse;
import it.polimi.ingsw.server.actions.data.WorkerViewResponse;

import java.util.Stack;

/**
 * ClientHandler execute commands from socket and send response to client.
 * Every Thread has his own ClientHandler, such as a Virtual Client
 */
public class ClientHandler implements ObserverBattlefield, ObserverWorkerView {

    private Controller controller;
    private ClientThread thread;
    private Stack<String> messageQueue;

    /**
     * Create ClientHandler
     * @param controller controller
     * @param thread thread
     */
    public ClientHandler(Controller controller, ClientThread thread){
       this.controller = controller;
       this.thread = thread;
       messageQueue = new Stack<>();
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
     * Send all message in queue
     */
    public void sendMessageQueue(){
       while(!messageQueue.empty())
           this.response(messageQueue.pop());
    }
    


    /**
     * Update received from subject observed
     * @param cellInterfaces matrix
     * @param message who update
     */
    @Override
    public void update(CellInterface[][] cellInterfaces, Message message) {
        response(new Gson().toJson(new BasicMessageResponse("battlefieldUpdate", new CellMatrixResponse(cellInterfaces))));
        System.out.println("Battlefield Updated!");

    }

    @Override
    public void update(boolean[][] workerView) {
        response(new Gson().toJson(new BasicMessageResponse("workerViewUpdate", new WorkerViewResponse(workerView))));
        System.out.println("WorkerView Updated!");
    }

    /**
     * Add message to queue
     * @param message
     */
    public void responseQueue(String message) {
        this.messageQueue.add(message);
    }


}
