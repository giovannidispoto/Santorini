package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.client.cli.CLIController;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.SantoriniException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Player2Test {

    public static void main(String[] args){
        BattlefieldClient battlefieldClient = BattlefieldClient.getBattlefieldInstance();
        View userInterface;
        String cliColor = "dark";
        ClientController clientController = new ClientController();

        String serverName1 = "127.0.0.3";
        String player2 = "Marcus";

        File file = new File("player2.txt");
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
            System.out.println("Game Ended, error: " + e.getMessage());
        }
    }

}
