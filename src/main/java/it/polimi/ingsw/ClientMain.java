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
                //Connection with server is UP
                clientController.waitSetPickedCards();
                //Woke up by: setPickedCards
                clientController.getDeckRequest();
                //Woke up by: getDeckResponse

                if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
                    commandLine.pickCards(clientController);
                }
                else{
                    commandLine.printGodPlayerActivity(clientController);
                }
                clientController.waitSetPlayerCard();
                //Woke up by: setPlayerCard
                //Player choose his card used in game
                commandLine.chooseCard(clientController);
            }
            //Launch GUI -> Santorini.jar gui
            else{
                GUIBuilder gui = new GUIBuilder();
            }
    }
}
