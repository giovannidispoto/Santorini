
package it.polimi.ingsw;

import java.io.IOException;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.*;

/**
 * Hello world!
 *
 */
public class ServerMain
{
    public static void main( String[] args )
    {
        Controller c = new Controller();
        Server server = new Server(1337,c);

        try {
            server.startServer();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }
}