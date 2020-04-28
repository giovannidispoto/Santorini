package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIBuilder;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.gui.GUIBuilder;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        ClientController clientController = new ClientController();
        clientController.startNetwork();

        //Stuff for tests
        String cliColor=""; //remove "dark"
        CLIBuilder commandLine = new CLIBuilder(cliColor,clientController);
        commandLine.setupConnection(clientController);
        clientController.waitSetPickedCards();
        System.out.print("SONO USCITO DALLA WAIT\n");
        if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
            commandLine.pickCards(clientController);
        }
        else{
            commandLine.purposeCall("godchoice");
        }
        clientController.waitSetPlayerCard();
        commandLine.chooseCard(clientController);
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
