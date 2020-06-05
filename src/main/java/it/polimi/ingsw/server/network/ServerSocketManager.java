package it.polimi.ingsw.server.network;

import it.polimi.ingsw.server.lobbyUtilities.LobbyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        System.out.println("Server socket ready on port: "+ansiGREEN+port+ansiRESET+nextLine);
        System.out.println(serverReadyMessage);
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
            System.out.println(ansiGREEN + "Your External IP :"+ansiRESET+nextLine+nextLine+"Address: "+ansiRED+getExternalIP()+ansiRESET+nextLine);
        } catch (IOException e) {
            System.out.println(ansiRED + "Can't Get Your External IP"+ansiRESET+nextLine);
        }

        try {
            System.out.println(ansiGREEN+"Your Local IP :"+ansiRESET+nextLine);
            getLocalIPInterfaces();
        } catch (SocketException e){
            System.out.println(ansiRED+"Can't Get Your Local IP"+ansiRESET+nextLine);
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
        int ipFoundCounter=0;
        List<InetAddress> localIpList = new ArrayList<>();

        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            if( inetAddress.isSiteLocalAddress()) {
                localIpList.add(inetAddress);
                ipFoundCounter++;
            }
        }
        if(ipFoundCounter>0){
            System.out.println("Interface name: " + networkInterface.getDisplayName());
            System.out.println("Name: " + networkInterface.getName());
            for(InetAddress localAddress : localIpList){
                System.out.println("LocalAddress: "+ansiGREEN+localAddress.getHostAddress()+ansiRESET);
            }
            System.out.println();
        }
    }

}