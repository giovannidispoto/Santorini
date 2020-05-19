package it.polimi.ingsw;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CLIController;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.gui.GUIController;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        final String darkCLI = "dark";
        final String lightCLI = "light";
        final String userChoiceCLI = "cli";
        //Create client objects
        View userInterface;
        String cliColor;
        ClientController clientController = new ClientController();

        //Default Option (no args) = GUI
        // Launch CLI -> Santorini.jar cli dark || Santorini.jar cli light
        if( (args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase(userChoiceCLI) || true){
            //Choose CLI color
            if(args.length == 2 && args[1].equalsIgnoreCase(lightCLI)) {
                cliColor = lightCLI;
            }
            else {
                cliColor = darkCLI;
            }
            //Start CLI
            Scanner consoleScanner = new Scanner(System.in);
            userInterface = new CLIController(cliColor, clientController, consoleScanner);
        }
        //Launch GUI -> Santorini.jar || Santorini.jar gui (etc)
        else{
            userInterface = new GUIController(clientController);
        }

        clientController.setUserView(userInterface);
        clientController.initializeNetwork();

        try {
            userInterface.startGame();
        }catch (SantoriniException e){
            if(e.getMessage().equals(ExceptionMessages.winMessage) || e.getMessage().equals(ExceptionMessages.loseMessage)) {
                userInterface.callMatchResult(e.getMessage());
            }
            else {
                userInterface.callErrorMessage(e.getMessage());
            }

            //TODO: testing
            if(Thread.interrupted()){
                clientController.loggerIO.severe("Thread interrupted but now ready (test)\n");
            }

        }
    }
}
