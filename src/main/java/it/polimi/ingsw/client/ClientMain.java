package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIBuilder;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.ClientSocketConnection;
import it.polimi.ingsw.controller.Controller;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        ClientController clientController = new ClientController();
        ClientSocketConnection socket = new ClientSocketConnection(clientController);

        //CLI
        CLIBuilder cli = new CLIBuilder();
        cli.setServerInformations(socket,clientController);

        /*
            if(args[0].equals("cli")){
            }
            else{
            }
         */
    }
}
