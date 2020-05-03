package it.polimi.ingsw;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CliController;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.gui.GuiController;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        View userInterface;
        String cliColor = "dark";
        ClientController clientController = new ClientController();

        //Default Option (no args) = cli & dark interface
        // Launch CLI -> Santorini.jar cli dark || Santorini.jar cli white
        if(args.length == 0 || args[0].equals("cli")){
            if(args.length == 2 && args[1].equals("light")) {
                cliColor = "light";
            }
            userInterface = new CliController(cliColor,clientController);
        }
        //Launch GUI -> Santorini.jar gui
        else{
            userInterface = new GuiController(clientController);
        }

        clientController.initializeNetwork();
        userInterface.startGame();
    }
}
