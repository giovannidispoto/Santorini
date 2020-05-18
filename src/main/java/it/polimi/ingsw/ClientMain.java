package it.polimi.ingsw;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CLIController;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.gui.GUIController;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        //Create client objects
        View userInterface;
        String cliColor = "dark";
        //force use GUI
        boolean forceGUI = true;
        ClientController clientController = new ClientController();

        //Default Option (no args) = cli & dark interface
        // Launch CLI -> Santorini.jar cli dark || Santorini.jar cli white
        if((args.length == 0 || args[0].equals("cli")) && !forceGUI){
            if(args.length == 2 && args[1].equals("light")) {
                cliColor = "light";
            }
            Scanner consoleScanner = new Scanner(System.in);
            userInterface = new CLIController(cliColor, clientController, consoleScanner);
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
            if(e.getMessage().equalsIgnoreCase("You have won! 👑") || e.getMessage().equalsIgnoreCase("You have lost! 😡"))
                userInterface.callMatchResult(e.getMessage());
            else
                userInterface.callErrorMessage(e.getMessage());
            //System.out.println("Game Ended : " + e.getMessage());

            if(Thread.interrupted()){
                System.out.println("Hi i've been interrupted, but now i'm ready :)");
            }
            System.exit(0);
        }
    }
}
