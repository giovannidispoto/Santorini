package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.network.action.CommandFactory;

public class ClientHandler implements Observer {

    private Controller c;

    public ClientHandler(){
        c = new Controller();
    }

    public void process(String m){
        CommandFactory.from(m).execute(c);
    }

    @Override
    public void update() {

    }
}
