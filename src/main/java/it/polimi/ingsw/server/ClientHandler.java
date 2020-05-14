package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.actions.CommandFactory;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.CellInterface;
import it.polimi.ingsw.server.actions.data.CellMatrixResponse;
import it.polimi.ingsw.server.actions.data.WorkerViewResponse;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ClientHandler execute commands from socket and send response to client.
 * Every Thread has his own ClientHandler, such as a Virtual Client
 */
public class ClientHandler implements ObserverBattlefield, ObserverWorkerView {

    private Controller controller;
    private ClientThread thread;
    private Stack<String> messageQueue;
    private Timer timeout;

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

    public void setTimer(){
        Timer timer = new Timer();
        timeout = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                response(new Gson().toJson(new BasicMessageResponse("ping", null)));
                timeout.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Timeout");
                    }
                }, 2000);

            }
        },4000);
    }

    public void resetTimeout(){
        timeout.cancel();
    }

    /**
     * Process command received from socket
     * @param m message
     */
    public void process(String m){
        try {
            CommandFactory.from(m).execute(controller, this);
        }catch(JsonParseException e){
            System.out.println(e.getMessage());
        }
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
     * Send Battlefield when observer notify change
     * @param cellInterfaces matrix
     */
    @Override
    public void update(CellInterface[][] cellInterfaces) {
        response(new Gson().toJson(new BasicMessageResponse("battlefieldUpdate", new CellMatrixResponse(cellInterfaces))));
        System.out.println("Battlefield Updated!");

    }

    /**
     * Send worker view when observer notify change
     * @param workerView workerView
     */
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
