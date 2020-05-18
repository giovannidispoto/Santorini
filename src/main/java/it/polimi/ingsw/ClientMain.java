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
        //Create client objects
        View userInterface;
        String cliColor = "dark";
        ClientController clientController = new ClientController();

        //Default Option (no args) = gui
        //Launch GUI -> Santorini.jar
        if(args.length == 0 || (args.length == 1 && args[0].equals("gui"))){
            userInterface = new GUIController(clientController);
        }
        // Launch CLI -> Santorini.jar cli dark || Santorini.jar cli light
        else{
            if(args.length == 2 && args[0].equals("cli") && args[1].equals("light")) {
                cliColor = "light";
            }
            Scanner consoleScanner = new Scanner(System.in);
            userInterface = new CLIController(cliColor, clientController, consoleScanner);
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


            //TODO: clean
            //System.out.println("Game Ended : " + e.getMessage());

            if(Thread.interrupted()){
                System.out.println("Hi i've been interrupted, but now i'm ready :)");
            }
            System.exit(0);
        }
    }
}
