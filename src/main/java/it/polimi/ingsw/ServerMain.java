
package it.polimi.ingsw;

import it.polimi.ingsw.server.LobbyManager;
import it.polimi.ingsw.server.ServerSocketManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.PrinterClass.ansiRED;
import static it.polimi.ingsw.PrinterClass.ansiRESET;

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
            do{
                System.out.println(ansiRED + "To close Server, write EXIT, at any moment\n"+ ansiRESET);
            }while(!System.console().readLine().equalsIgnoreCase("EXIT"));
            System.exit(0);
        });

        try {
            serverSocketManager.startServerSocket();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}