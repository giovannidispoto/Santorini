package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.network.action.CommandFactory;

public class ClientHandler implements Observer {

    private Controller controller;
    private ClientThread thread;

    public ClientHandler(Controller controller, ClientThread thread){
       this.controller = controller;
       this.thread = thread;
    }

    public void process(String m){
        CommandFactory.from(m).execute(controller, this);
    }

    public void response(String m){
        this.thread.send(m);
    }

    @Override
    public void update(Message message) {
        switch (message){
            case WORKERVIEW:
                System.out.println("workerView Updated!");
                break;
            case BATTLEFIELD:
                System.out.println("Battlefield Updated!");
                break;
        }

    }
}
