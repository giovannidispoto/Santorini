
package it.polimi.ingsw;

import it.polimi.ingsw.server.LobbyManager;
import it.polimi.ingsw.server.ServerSocketManager;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.PrinterClass.*;

/**
 * Hello world!
 *
 */
public class ServerMain
{
    public static void main( String[] args )
    {
        FileManager serverFileManager= new FileManager();
        ExecutorService ServerExit = Executors.newFixedThreadPool(1);
        ServerSocketManager serverSocketManager = new ServerSocketManager(1337, new LobbyManager());

        //Start Server Testing - Essential Files
        serverFileManager.testFileReading();

        ServerExit.execute(() -> {
            Scanner inConsole = new Scanner(System.in);
            do{
                System.out.println(ansiRED + "To close Server, write EXIT, at any moment"+ ansiRESET+nextLine);
            }while(!inConsole.nextLine().equalsIgnoreCase("EXIT"));
            System.exit(0);
        });

        try {
            serverSocketManager.startServerSocket();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}