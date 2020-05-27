package it.polimi.ingsw.server;

import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static it.polimi.ingsw.server.consoleUtilities.PrinterClass.*;

/**
 * Server Class
 */
public class ServerSocketManager {

    private final int port;
    private final LobbyManager lobbyManager;

    /**
     * Create new server at selected port.
     * Controller is one instance in all the application, so is created in main class and passed
     * @param port port
     * @param lobbyManager controller
     */
    public ServerSocketManager(int port, LobbyManager lobbyManager){
        this.port = port;
        this.lobbyManager = lobbyManager;
    }

    /**
     * Start Server
     * @throws IOException exception
     */
    public void startServerSocket() throws IOException {
        printIPAddresses();
        //open TCP port
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.printf("Server socket ready on port: %s %d %s\n\n" , ansiGREEN, port, ansiRESET);
        System.out.println(ansiGREEN + "Server READY\n" + ansiRESET + nextLine + consoleSeparator);
        //wait for connection
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("Received client connection");
            // open input and output streams to read and write
            Thread thread = new Thread(new ClientThread(socket, lobbyManager));
            thread.start();
        }

    }


    //GET & PRINT IP-----------------------------------------------------------------------------------------

    private void printIPAddresses(){
        try {
            System.out.printf(ansiGREEN + "Your External IP is : %s %s %s\n\n", ansiRED, getExternalIP(), ansiRESET);
        } catch (IOException e) {
            System.out.println(ansiRED + "Can't Get Your External IP\n" + ansiRESET);
        }

        try {
            System.out.println(ansiGREEN+"Your Local IP :\n"+ ansiRESET);
            getLocalIPInterfaces();
        } catch (SocketException e){
            System.out.println(ansiRED+"Can't Get Your Local IP\n"+ ansiRESET);
        }
    }

    private String getExternalIP() throws IOException {
        URL checkURL = new URL("http://checkip.amazonaws.com");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(checkURL.openStream()))) {
            return in.readLine();
        }
    }

    private void getLocalIPInterfaces() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface networkInterface : Collections.list(nets))
            displayInterfaceInformation(networkInterface);
    }

    private static void displayInterfaceInformation(NetworkInterface networkInterface) {
        int ipFound=0;
        PrintStream out = System.out;
        List<InetAddress> localIpList = new ArrayList<>();

        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if( inetAddress.isSiteLocalAddress()) {
                localIpList.add(inetAddress);
                ipFound++;
            }
        }
        if(ipFound>0){
            out.printf("Interface name: %s\n", networkInterface.getDisplayName());
            out.printf("Name: %s\n", networkInterface.getName());
            for(InetAddress localAddress : localIpList){
                out.printf("LocalAddress: %s %s %s\n", ansiGREEN, localAddress.getHostAddress(), ansiRESET);
            }
            out.print("\n");
        }
    }

}