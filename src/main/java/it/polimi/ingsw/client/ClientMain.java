package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLIBuilder;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.network.ClientSocketConnection;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        ClientController clientController = new ClientController();
        clientController.startNetwork();

        //CLI
        CLIBuilder cli = new CLIBuilder();
        cli.setServerInformations(clientController.getSocketConnection(),clientController);

        /*
            if(args[0].equals("cli")){
            }
            else{
            }
         */
    }
}
