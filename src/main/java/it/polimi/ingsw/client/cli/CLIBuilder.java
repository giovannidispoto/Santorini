package it.polimi.ingsw.client.cli;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.UIActions;
import it.polimi.ingsw.client.network.ClientSocketConnection;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * CLIBuilder contains everything you need to build the CLI and use it
 */
public class CLIBuilder implements UIActions {

    //ANSI Colors
    private static final String CODE_BLUE ="33";
    private static final String CODE_LIGHTBLUE ="75";
    private static final String CODE_BROWN ="130";
    private static final String CODE_GRAY ="252";
    private static final String CODE_WHITE ="255";
    private static final String CODE_RED = "197";
    private static final String CODE_PURPLE = "105";
    private static final String CODE_GREEN = "41";
    private static final String ANSI_PRFX ="\u001b[38;5;";
    protected static final String ANSI_RST = "\u001b[0m";

    protected static final String ANSI_BLUE = ANSI_PRFX+CODE_BLUE+"m";
    protected static final String ANSI_LIGHTBLUE = ANSI_PRFX+CODE_LIGHTBLUE+"m";
    protected static final String ANSI_BROWN = ANSI_PRFX+CODE_BROWN+"m";
    protected static final String ANSI_GRAY = ANSI_PRFX+CODE_GRAY+"m";
    protected static final String ANSI_WHITE = ANSI_PRFX+CODE_WHITE+"m";
    protected static final String ANSI_PURPLE = ANSI_PRFX+CODE_PURPLE+"m";
    protected static final String ANSI_RED = ANSI_PRFX+CODE_RED+"m";
    protected static final String ANSI_GREEN = ANSI_PRFX+CODE_GREEN+"m";

    //Data Objects
    private CLIDataObject[] boardCellsContents; // row ┃   ┃   ┃   ┃   ┃   ┃
    private List<String> playerMoves;
    private String currentPhase;
    private int numberFullTowers;

    //UI Objects
    protected static final String CLI_INPUT = "> ";
    protected static final String NEW_LINE = "\n";
    protected static final String BLANK = " ";
    private static final String BOARD_TITLE = "BOARD";
    private static final String PLAYERS_TITLE = "PLAYERS";
    private static final String TOWERS_TITLE = "FULL TOWERS";
    private static final String MOVES_TITLE = "PLAYER MOVES";
    private static final String PHASE_TITLE = "CURRENT PHASE";

    //UI Messages
    private static final String WAITING_ALERT = "Wait for your turn...";

    //Board Matrix
    protected static final String L_T_CORNER = "┏";
    protected static final String R_T_CORNER = "┓";
    protected static final String L_B_CORNER = "┗";
    protected static final String R_B_CORNER = "┛";
    protected static final String R_LAT_SEPARATOR = "┫";
    protected static final String L_LAT_SEPARATOR = "┣";
    protected static final String INT_SEPARATOR = "╋";
    protected static final String U_LAT_SEPARATOR = "┳";
    protected static final String LO_LAT_SEPARATOR = "┻";
    protected static final String H_LINE = "━";
    protected static final String V_LINE = "┃";

    //Messages Box
    protected static final String L_THIN_T_CORNER = "┌";
    protected static final String R_THIN_T_CORNER = "┐";
    protected static final String L_THIN_B_CORNER = "└";
    protected static final String R_THIN_B_CORNER = "┘";
    protected static final String H_THIN_LINE = "─";
    protected static final String V_THIN_LINE = "│";
    protected static final String DOT_H_LINE = "╌";
    protected static final String DOT_V_LINE = "┊";

    //ANSI Cursor Moves
    protected static final String CURSOR_UP = "\u001b[%sA";
    protected static final String CURSOR_DWN = "\u001b[%sB";
    protected static final String CURSOR_LFT = "\u001b[%sD";
    protected static final String CURSOR_RGT = "\u001b[%sC";

    //ANSI Special Sequences
    protected static final String CLEAN = "\u001b[0J";

    //Templates
    private static final String upperEdgeBoard =
            L_T_CORNER+H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+U_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+R_T_CORNER;
    private static final String intermediateEdgeBoard =
            L_LAT_SEPARATOR+H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+INT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+R_LAT_SEPARATOR;
    private static final String lowerEdgeBoard =
            L_B_CORNER+H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+LO_LAT_SEPARATOR+
            H_LINE+H_LINE+H_LINE+R_B_CORNER;
    private static final String edge_distance = BLANK+BLANK+BLANK;
    private static final String playerInformations = " %s %s|%s "; // ᳵ SteveJobs|Athena
    private static final String playerMove = " [%s|%s] "; // [1|2]

    //Sizes
    private int rowCounter;
    private final int refreshable_area_height = 15;
    private final int editable_board_rows = 5;

    //Web utilities
    private static final String SERVERIP = "Server IP 🌍";
    private static final String NICKNAME = "Nickname 👾";
    private static final String SETUPTITLE = "Setup Connection";
    private static final String HANDSHAKING = "Handshaking with %s on port %s...";
    private static final String WAITSTART = "Wait for the match startup...";
    private static final String NICKNAMEERROR = "An user with this nickname already exists... retry!";
    private static final String LOBBYSIZEERROR = "This game is for 2 or 3 players... retry!";
    private static final String SETPLAYERS = "Number of players 👦🏼";
    private HashMap<Integer,Color> COLORSMAP;

    //Players Information Box
    protected static final String WORKER = "◈";
    private HashMap<Color,String> coloredWorker;

    //Cards
    private static final String cardTemplate = "• %s | %s";

    /**
     * Class Constructor
     */
    public CLIBuilder() {
        this.boardCellsContents = new CLIDataObject[5];
        this.playerMoves = new ArrayList<>();
        this.COLORSMAP = new HashMap<>();
        this.coloredWorker = new HashMap<>();
        this.currentPhase = null;
        this.numberFullTowers = 0;
        this.rowCounter = 0;
        COLORSMAP.put(0,Color.BLUE);
        COLORSMAP.put(1,Color.GREY);
        COLORSMAP.put(2,Color.BROWN);
        coloredWorker.put(Color.BLUE,ANSI_BLUE+WORKER);
        coloredWorker.put(Color.BROWN,ANSI_BROWN+WORKER);
        coloredWorker.put(Color.GREY,ANSI_GRAY+WORKER);
    }

    /**
     * Prints a message in a colored box
     * @param message is the string that has to be print
     * @param messageColor is the color of the box and the message
     */
    public void renderMessageBox(String messageColor, String message){
        int messageLength = message.length();
        System.out.print(messageColor+L_THIN_T_CORNER);
        //+2 to consider the blank spaces between the message and the lateral edges
        for(int i=0;i<messageLength+2;i++)
            System.out.print(H_THIN_LINE);
        System.out.println(R_THIN_T_CORNER);
        System.out.println(V_THIN_LINE+BLANK+message+BLANK+V_THIN_LINE);
        System.out.print(L_THIN_B_CORNER);
        for(int i=0;i<messageLength+2;i++)
            System.out.print(H_THIN_LINE);
        System.out.println(R_THIN_B_CORNER);
    }

    /**
     * Renders the players information box
     *      PLAYERS
     *      ┌────────────────────┐
     *      │ ◈ SteveJobs|Athena │
     *      │ ...                │
     *      └────────────────────┘
     */
    public void renderPlayersInfoBox(ClientController clientController){
        int maxLength = 0;
        String pieceOfString;
        List<String> playerInfo = new ArrayList<>();
        StringBuilder playerData = new StringBuilder();
        System.out.print(ANSI_WHITE+PLAYERS_TITLE+NEW_LINE);
        clientController.getPlayersRequest();
        //Build the players info strings
        for(PlayerInterface current : clientController.getPlayers()){
            pieceOfString= coloredWorker.get(current.getColor());
            playerData.append(V_THIN_LINE + BLANK).append(pieceOfString);
            pieceOfString = current.getPlayerNickname();
            playerData.append(BLANK).append(ANSI_WHITE).append(pieceOfString);
            pieceOfString = current.getCard();
            playerData.append(BLANK+"•"+BLANK).append(pieceOfString).append(BLANK+V_THIN_LINE);
            playerInfo.add(playerData.toString());}
        //Discover the longest string
        for(String current : playerInfo){
            if(current.length()>maxLength)
                maxLength=current.length();}
        //Print the box
        System.out.print(BLANK+BLANK+BLANK+ANSI_WHITE+PLAYERS_TITLE+NEW_LINE);
        System.out.print(BLANK+BLANK+BLANK+L_THIN_T_CORNER);
        for(int i=0;i<maxLength;i++)
            System.out.print(H_THIN_LINE);
        System.out.print(R_THIN_T_CORNER+NEW_LINE);
        for(String current : playerInfo){
            System.out.print(BLANK+BLANK+BLANK+current+NEW_LINE);
        }
        System.out.print(BLANK+BLANK+BLANK+L_THIN_B_CORNER);
        for(int i=0;i<maxLength;i++)
            System.out.print(H_THIN_LINE);
        System.out.print(R_THIN_B_CORNER+NEW_LINE+NEW_LINE);
    }

    /**
     * Renders the main part of the CLI
     *            BOARD                             0
     *      0   1   2   3   4     FULL TOWERS       1
     *    ┏━━━┳━━━┳━━━┳━━━┳━━━┓   ┌╌╌╌┐             2
     *  0 ┃   ┃   ┃   ┃   ┃   ┃   ┊ 4 ┊             3
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   └╌╌╌┘             4
     *  1 ┃   ┃   ┃   ┃   ┃   ┃   CURRENT PHASE     5
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   ┌╌╌╌╌╌╌╌╌╌╌┐      6
     *  2 ┃   ┃   ┃   ┃   ┃   ┃   ┊ Building ┊      7
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   └╌╌╌╌╌╌╌╌╌╌┘      8
     *  3 ┃   ┃   ┃   ┃   ┃   ┃   AVAILABLE MOVES   9
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫   ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐   10
     *  4 ┃   ┃   ┃   ┃   ┃   ┃   ┊ [2|1] [0|2] ┊   11
     *    ┗━━━┻━━━┻━━━┻━━━┻━━━┛   └╌╌╌╌╌╌╌╌╌╌╌╌╌┘   12
     *                                              13
     * > Type something...                          14 + 1 (press Enter)
     */
    public void renderCLI(){}

    /**
     *  Renders the available cards for the initial player choice
     *  • Chronos | You win if there are five full towers on the board
     */
    public void renderAvailableCards(){};

    /**
     * Renders the entire deck (list of cards)
     */
    public void renderDeck(){};

    //UI ACTION METHODS
    @Override
    public void chooseCard(ClientController clientController) {

    }

    @Override
    public void moveWorker(ClientController clientController) {

    }

    @Override
    public void buildBlock(ClientController clientController) {

    }

    @Override
    public void removeBlock(ClientController clientController) {

    }

    @Override
    public void selectWorker(ClientController clientController) {

    }

    @Override
    public void placeWorkers(ClientController clientController) {
    }

    @Override
    public void skipAction(ClientController clientController) {

    }

    @Override
    public void showCards(ClientController clientController) {

    }

    @Override
    public void setServerInformations(ClientSocketConnection clientSocket, ClientController clientController) {
        Scanner consoleScanner = new Scanner(System.in);
        String userInput;
        Color generatedColor;
        int userValue = 0;
        boolean connectionStatus = false;
        boolean validUsername = true;
        renderMessageBox(ANSI_PURPLE,SETUPTITLE);
        /*  # Initial Client - Server Handshake #
            Server IP 🌍
            >
            Handshaking with 192.168.1.9 on port 1337...
         */
        System.out.print(ANSI_WHITE+SERVERIP+NEW_LINE+CLI_INPUT);
        userInput=consoleScanner.nextLine();
        clientSocket.setServerName(userInput);
        System.out.print(String.format(HANDSHAKING,clientSocket.getServerName(),clientSocket.getServerPort())+NEW_LINE);
        while(!connectionStatus){
            if(!clientSocket.startConnection()){
                /*  # Handshake error #
                    Server IP 🌍
                    >
                    Handshaking with 1.2.3.4 on port 1337...
                    Connection error...retry!
                    > |
                */
                System.out.print(ANSI_RED+"Connection error...retry!"+NEW_LINE+ANSI_WHITE+CLI_INPUT);
                userInput=consoleScanner.nextLine();
                clientSocket.setServerName(userInput);
                System.out.print(String.format(CURSOR_UP,3));
                System.out.print(CLEAN);
                System.out.print(String.format(HANDSHAKING,clientSocket.getServerName(),clientSocket.getServerPort())+NEW_LINE);

            }
            else{
                /*  # Handshake success #
                    Server IP 🌍
                    >
                    Handshaking with 192.168.1.9 on port 1337...
                    Connection established!
                */
                connectionStatus=true;
                System.out.print(ANSI_GREEN+"Connection established!"+NEW_LINE);
            }
        }
        /*  # Nickname setup #
            Server IP 🌍
            >
            Handshaking with 192.168.1.9 on port 1337...
            Connection established!
            Nickname 👾
            > |
        */
        System.out.print(ANSI_WHITE+NICKNAME+NEW_LINE+CLI_INPUT);
        userInput=consoleScanner.nextLine();
        clientController.getPlayersRequest();
        for(PlayerInterface current : clientController.getPlayers()){
            if(current.getPlayerNickname().equals(userInput))
                validUsername=false;
        }
        while(!validUsername){
            /*  # Nickname setup error #
                Server IP 🌍
                >
                Handshaking with 192.168.1.9 on port 1337...
                Connection established!
                Nickname 👾
                >
                An user with this nickname already exists... retry!
                >
            */
            System.out.print(ANSI_RED+NICKNAMEERROR+NEW_LINE+ANSI_WHITE+CLI_INPUT);
            userInput=consoleScanner.nextLine();
            validUsername=true;
            for(PlayerInterface current : clientController.getPlayers()){
                if(current.getPlayerNickname().equals(userInput))
                    validUsername=false;
            }
            System.out.print(String.format(CURSOR_UP,2));
            System.out.print(CLEAN);
        }
        clientController.setPlayerNickname(userInput);
        //Add new player to the server lobby
        clientController.getPlayersRequest();
        generatedColor=COLORSMAP.get(clientController.getPlayers().size());
        //clientController.addPlayerRequest(clientController.getPlayerNickname(),generatedColor);
        //Optional lobby size set
        clientController.getPlayersRequest();
        System.out.println("Numero giocatori sul server: "+clientController.getPlayers().size());
        if(clientController.getPlayers().size()==1){
            /*  # Lobby size setup #
                Server IP 🌍
                >
                Handshaking with 192.168.1.9 on port 1337...
                Connection established!
                Nickname 👾
                >
                You're the first player in the lobby! Set the number of players for this match 👦🏼
                >
                Wait for the match startup... |
            */
            System.out.print(ANSI_WHITE+SETPLAYERS+NEW_LINE+CLI_INPUT);
            userValue=consoleScanner.nextInt();
            while(userValue!=2 && userValue!=3){
                /*  # Lobby size setup #
                    Server IP 🌍
                    >
                    Handshaking with 192.168.1.9 on port 1337...
                    Connection established!
                    Nickname 👾
                    >
                    You're the first player in the lobby! Set the number of players for this match 👦🏼
                    >
                    This game is for 2 or 3 players... retry!
                    >
                */
                System.out.print(ANSI_RED+LOBBYSIZEERROR+NEW_LINE+ANSI_WHITE+CLI_INPUT);
                userValue=consoleScanner.nextInt();
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
            }
            //clientController.setLobbySizeRequest(clientController.getPlayerNickname(),userValue);
            System.out.print(WAITSTART);
            rowCounter = 8;
        }
        else{
             /*
                Server IP 🌍
                >
                Handshaking with 192.168.1.9 on port 1337...
                Connection established!
                Nickname 👾
                >
                Wait for the match startup... |
            */
            System.out.print(WAITSTART);
            rowCounter = 6;
        }
    }


}
