package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CLIController;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.ExceptionMessages;
import it.polimi.ingsw.client.controller.SantoriniException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Internal test for debugging, allows you to load the cli stream from a text file and run the same game or multiple games several times
 * The text files must be properly compiled following the requests of the CLI
 */
public class Player2Test {

    public static void main(String[] args){
        //BattlefieldClient battlefieldClient = BattlefieldClient.getBattlefieldInstance();
        View userInterface;
        String cliColor = "dark";
        ClientController clientController = new ClientController();

        //String serverName1 = "127.0.0.3";
        //String player2 = "Marcus";

        File file = new File("src/test/java/it/polimi/ingsw/client/network/player2.txt");
        Scanner consoleScanner = null;
        try {
            consoleScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        userInterface = new CLIController(cliColor,clientController, consoleScanner);

        //StartGame

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
        }
    }

}
