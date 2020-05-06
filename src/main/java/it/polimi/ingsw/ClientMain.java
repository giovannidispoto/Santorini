package it.polimi.ingsw;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CLIController;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.gui.GUIController;

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
            userInterface = new CLIController(cliColor,clientController);
        }
        //Launch GUI -> Santorini.jar gui
        else{
            userInterface = new GUIController(clientController);
        }

        clientController.setUserView(userInterface);
        clientController.initializeNetwork();

        try {
            userInterface.startGame();
        }catch (SantoriniException e){
            System.out.println("Game Ended, error: " + e.getMessage());
        }
    }
}
