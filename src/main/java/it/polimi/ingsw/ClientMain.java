package it.polimi.ingsw;

import it.polimi.ingsw.client.cli.CLIBuilder;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.gui.GUIBuilder;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        String cliColor="dark";
        ClientController clientController = new ClientController();
        clientController.startNetwork();
        //Launch CLI -> Santorini.jar cli dark || Santorini.jar cli white
            if(args[0].equals("cli")){
                if(args[1]!=null)
                    if(args[1].equals("light"))
                        cliColor="light";
                CLIBuilder commandLine = new CLIBuilder(cliColor,clientController);
                commandLine.setupConnection(clientController);
                clientController.waitSetPickedCards();
                if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
                    commandLine.pickCards(clientController);
                }
                else{
                    commandLine.printGodPlayerActivity(clientController);
                }
                clientController.waitSetPlayerCard();
                commandLine.chooseCard(clientController);
            }
            //Launch GUI -> Santorini.jar gui
            else{
                GUIBuilder gui = new GUIBuilder();
            }
    }
}
