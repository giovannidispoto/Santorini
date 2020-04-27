package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIBuilder;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.gui.GUIBuilder;
import it.polimi.ingsw.client.network.ClientSocketConnection;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        ClientController clientController = new ClientController();
        clientController.startNetwork();

        //Stuff for tests
        String cliColor=""; //remove "dark"
        CLIBuilder commandLine = new CLIBuilder(cliColor);
        commandLine.setupConnection(clientController);
        clientController.waitSetPickedCards();
        commandLine.pickCards(clientController);
        /*
            //Launch CLI -> Santorini.jar cli dark || Santorini.jar cli white
            if(args[0].equals("cli")){
                String colorScheme;
                if(args[1]!=null)
                    colorScheme=args[1];
                else
                    colorScheme="dark";
                CLIBuilder cli = new CLIBuilder(colorScheme);
            }
            //Launch GUI -> Santorini.jar gui
            else{
                GUIBuilder gui = new GUIBuilder();
            }

         */
    }
}
