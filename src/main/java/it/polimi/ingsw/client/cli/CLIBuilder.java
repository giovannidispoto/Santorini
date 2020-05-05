package it.polimi.ingsw.client.cli;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.UIActions;
import it.polimi.ingsw.client.network.data.dataInterfaces.PlayerInterface;

import java.util.*;

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
    private static final String CODE_LIME = "83";
    private static final String CODE_BLACK = "232";
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
    protected static final String ANSI_BLACK = ANSI_PRFX+CODE_BLACK+"m";
    protected static final String ANSI_LIME = ANSI_PRFX+CODE_LIME+"m";
    private static String COLORMODE;

    //Game data
    private CLIDataObject[] boardCellsContents; // row ┃   ┃   ┃   ┃   ┃   ┃
    private List<String> playerMoves;
    private String currentPhase;
    private int numberFullTowers;

    //UI Objects
    protected static final String CLI_INPUT = "> ";
    protected static final String NEW_LINE = "\n";
    protected static final String BLANK = " ";
    private static final String BOARD_TITLE = "BOARD";
    private static final String HORIZONTAL_ROW_NUMBER = "  0 "+"  1  "+" 2  "+" 3  "+" 4  ";
    private static final String PLAYERS_TITLE = "PLAYERS 👦🏼";
    private static final String TOWERS_TITLE = "FULL TOWERS 🏗";
    private static final String MOVES_TITLE = "PLAYER MOVES 🕹";
    private static final String PHASE_TITLE = "CURRENT PHASE 🚀";

    //Web utilities
    private static final String SETUPTITLE = "Setup Connection";

    private static final String SOCKET_PORT = "Socket Port ⛩";
    private static final String PORT_SUGGESTION = "We suggest you the port 1337 • ";
    private static final String SERVER_IP = "Server IP 🌍";
    private static final String NICKNAME = "Nickname 👾";
    private static final String LOBBY_SIZE = "Lobby Size 📦";
    private static final String LOBBY_JOIN = "Joining the lobby...";
    private static final String CONNECTION_HUNT = "Connection lost! Looking for connection...🦖";
    private static final String HANDSHAKING = "Handshaking with %s on port %s...🦖 ";
    private static final String WAIT_PLAYERS = "Wait that the others players make their choice...";

    private static final String SUCCESS_HANDSHAKING = "Connection established!";
    private static final String SUCCESS_LOBBY_ACCESS = "You have correctly joined the lobby!";

    private static final String INVALID_IP = "Invalid IP...retry! • ";
    private static final String PORT_SUGGESTION_ERROR = "This is not a number! We suggest you the port 1337 • ";
    private static final String FAILED_CONNECTION = "Troubles with the connection...retry!";
    private static final String LOBBY_SIZE_ERROR = "This game is just for 2 or 3 people...retry! • ";
    private static final String INVALID_LOBBY_SIZE = "This is not a number...retry! • ";
    private static final String UNAVAILABLE_LOBBY = "The selected lobby is full or unavailable... try later 😭";
    private static final String NICKNAME_ERROR = "There is already a player with this nickname in the lobby...retry!";


    private static final String WAITSTART = "Wait for the match startup...";

    //Players Information Box
    protected static final String WORKER = "◈";
    private HashMap<Color,String> WorkerColorsMap;

    //Cards
    private static final String PICK_TITLE = "Cards Pick Up";
    private static final String CHOICE_TITLE = "Card Choice";
    private static final String CHOOSE_CARD = "Choose your card for this match 🕹";
    private static final String INVALID_CARD = "Invalid card choice...retry! • ";

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

    //ANSI Cursor Moves
    protected static final String CURSOR_UP = "\u001b[%sA";
    protected static final String CURSOR_DWN = "\u001b[%sB";
    protected static final String CURSOR_LFT = "\u001b[%sD";
    protected static final String CURSOR_RGT = "\u001b[%sC";

    //ANSI Special Sequences
    protected static final String CLEAN = "\u001b[0J";

    // # STRING TEMPLATES #
    //Board templates
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

    //Full Towers Box templates
    private static final String upperEdgeTowers = L_THIN_T_CORNER+H_THIN_LINE+H_THIN_LINE+H_THIN_LINE+R_THIN_T_CORNER;
    private static final String intermediateEdgeTowers = V_THIN_LINE+BLANK+"%s"+BLANK+V_THIN_LINE;
    private static final String lowerEdgeTowers = L_THIN_B_CORNER+H_THIN_LINE+H_THIN_LINE+H_THIN_LINE+R_THIN_B_CORNER;

    //General purpose templates
    private static final String playerDataTemplate = " %s %s|%s ";
    private static final String playerMoveTemplate = " [%s|%s] ";
    private static final String pickChoiceTemplate = "Your %s choice • ";
    private static final String cardTemplate = "• %s | %s";
    private static final String PICK_CARDS = "You're the player chosen by the gods! Choose %s cards for this match 👑";

    //CLI Sizes and Counters
    private int godPlayerRefreshHeight;
    private int printedLinesCounter;
    private final int boardTitleEdgeDistance = 12;
    private final int horizontalRowNumberDistance = 4;
    private final int edgeDistance = 4;
    private final int refreshable_area_height = 14;
    private final int editable_board_rows = 5;

    //General Purpose Commands
    private static final String GOD_MESSAGE = "%s is picking up the cards for this match...";

    /**
     * Class Constructor
     */
    public CLIBuilder(String colorMode, ClientController clientController) {
        this.boardCellsContents = new CLIDataObject[5];
        this.playerMoves = new ArrayList<>();
        this.WorkerColorsMap = new HashMap<>();
        this.currentPhase = null;
        this.numberFullTowers = 0;
        this.godPlayerRefreshHeight=0;
        this.printedLinesCounter = 0;
        //Initial color scheme setup
        if(colorMode.equals("light")){
            COLORMODE=ANSI_BLACK;
        }
        else{
            COLORMODE=ANSI_WHITE;
        }
        //Generate colored workers based on the player color
        WorkerColorsMap.put(Color.BLUE,ANSI_BLUE+WORKER);
        WorkerColorsMap.put(Color.BROWN,ANSI_BROWN+WORKER);
        WorkerColorsMap.put(Color.GREY,ANSI_GRAY+WORKER);
    }

    //TODO: Already polished code

    /**
     *  Renders the available cards for the initial player choice
     *  • CHRONUS | Owner win if there are five full towers on the board
     */
    public void renderAvailableCards(ClientController clientController){
        for(String card : clientController.getGodCards()){
            System.out.println(String.format(cardTemplate,ANSI_LIGHTBLUE+clientController.getCardsDeck().getDivinityCard(card).getCardName().toUpperCase(),ANSI_WHITE+clientController.getCardsDeck().getDivinityCard(card).getCardEffect()));
            printedLinesCounter+=1;
        }
    };

    /**
     *  Renders the entire deck (list of cards)
     *  • CHRONUS | effect...
     *  ...
     *  • ZEUS | effect...
     */
    public void renderDeck(ClientController clientController){
        clientController.getDeckRequest();
        for(DivinityCard current : clientController.getCardsDeck().getAllCards()){
            System.out.println(String.format(cardTemplate,ANSI_LIGHTBLUE+current.getCardName().toUpperCase()+ANSI_WHITE,current.getCardEffect()));
            printedLinesCounter+=1;
        }
    };

    /**
     * Prints the passed message in a colored box
     * @param message is the string that has to be print
     * @param messageColor is the color of the box and the message
     */
    public void renderTitleBox(String messageColor, String message){
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
        System.out.print(ANSI_WHITE);
        printedLinesCounter+=3;
    }

    /**
     * Prints the upper edge of a game info box
     * @param message is the string that has to be print
     * @param messageColor is the color of the box and the message
     */
    public void renderGameInfoBoxUpperEdge(String messageColor, String message){
        int messageLength = message.length();
        System.out.print(messageColor+L_THIN_T_CORNER);
        //+2 to consider the blank spaces between the message and the lateral edges
        for(int i=0;i<messageLength+2;i++)
            System.out.print(H_THIN_LINE);
        System.out.println(R_THIN_T_CORNER);

    }

    /**
     * Prints the lower edge of a game info box
     * @param message is the string that has to be print
     * @param messageColor is the color of the box and the message
     */
    public void renderGameInfoBoxLowerEdge(String messageColor, String message){
        int messageLength = message.length();
        System.out.print(messageColor+L_THIN_B_CORNER);
        for(int i=0;i<messageLength+2;i++)
            System.out.print(H_THIN_LINE);
        System.out.println(R_THIN_B_CORNER);
        System.out.print(ANSI_WHITE);
    }

    /**
     * Prints notification about God player activity
     */
    public void printGodPlayerActivity(ClientController clientController){
        System.out.println(ANSI_LIME+String.format(GOD_MESSAGE,clientController.getGodPlayer())+ANSI_WHITE);
        printedLinesCounter+=1;
    }

    //UIActions METHODS
    /**
     * Allows the GodPlayer (the game's master player) to pick the cards for this match
     * @param clientController is the controller of the client
     */
    @Override
    public void pickCards(ClientController clientController) {
        //Local Variables
        boolean isValidInput = false;
        int numberOfPlayers=clientController.getCurrentLobbySize();
        int pickedCounter=0;
        Scanner consoleScanner = new Scanner(System.in);
        String userInput;
        List<String> chosenCards = new ArrayList<>();
        DivinityCard pickedCard;
        //Clean the CLI from the last phase elements
        System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        System.out.print(CLEAN);
        //Render graphic elements
        /*  # Cards Extraction #
            ┌───────────────┐
            │ Cards Pick Up │
            └───────────────┘
            • APOLLO | Your Move: Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated
            •
            •
            •
            •
            •
            •
            •
            •
            •
            •
            •
            •
            •
            •
            • ZEUS | Your Build: Your Worker may build a block under itself
            Choose 3 cards for this match 🎮
            Your 1 choice • >
         */
        printedLinesCounter=0;
        renderTitleBox(ANSI_PURPLE,PICK_TITLE);
        renderDeck(clientController);
        System.out.print(String.format(PICK_CARDS,numberOfPlayers)+NEW_LINE);
        //Multiple extraction
        while (pickedCounter<numberOfPlayers){
            System.out.print(String.format(pickChoiceTemplate,pickedCounter+1)+CLI_INPUT);
            userInput=consoleScanner.next().toUpperCase();
            isValidInput=true;
            if(!clientController.getCardsDeck().getCardsNames().contains(userInput))
                isValidInput=false;
            if(!chosenCards.isEmpty()){
                if(chosenCards.contains(userInput))
                    isValidInput=false;}
            while (!isValidInput){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_CARD+ANSI_WHITE+CLI_INPUT);
                userInput=consoleScanner.next().toUpperCase();
                isValidInput=true;
                if(!chosenCards.isEmpty()){
                    if(chosenCards.contains(userInput))
                        isValidInput=false;}
            }
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            pickedCounter++;
            chosenCards.add(userInput);
        }
        clientController.setPickedCardsRequest(chosenCards);
        System.out.print(ANSI_LIME+WAIT_PLAYERS+NEW_LINE);
        printedLinesCounter+=2;
        godPlayerRefreshHeight=printedLinesCounter;
    }

    /**
     * Allows the current Player to choose his card for the match between the available ones
     * @param clientController is the controller of the client
     */
    @Override
    public void chooseCard(ClientController clientController) {
        //Local Variables
        boolean validInput = true;
        Scanner consoleScanner = new Scanner(System.in);
        String userInput;
        List<String> availableCards = clientController.getGodCards();
        //Clean the CLI from the last phase elements
        if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
            System.out.print(String.format(CURSOR_UP,godPlayerRefreshHeight));
            System.out.print(CLEAN);
        }
        else{
            System.out.print(String.format(CURSOR_UP,printedLinesCounter));
            System.out.print(CLEAN);
        }
        //Render the graphic elements
        /*   # Card Selection #
            ┌─────────────┐
            │ Card Choice │
            └─────────────┘
            •
            •
            • ZEUS | Your Build: Your Worker may build a block under itself
            Choose your card for this match 🕹
            >
         */
        printedLinesCounter=0;
        renderTitleBox(ANSI_PURPLE,CHOICE_TITLE);
        renderAvailableCards(clientController);
        System.out.print(ANSI_WHITE+CHOOSE_CARD+NEW_LINE+CLI_INPUT);
        userInput=consoleScanner.next().toUpperCase();
        if(!availableCards.contains(userInput))
            validInput=false;
        while(!validInput){
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            /*  # Card Selection #
                •
                •
                • ZEUS | Your Build: Your Worker may build a block under itself
                Choose your card for this match 🕹
                Invalid card choice...retry! • >
            */
            System.out.print(ANSI_RED+INVALID_CARD+ANSI_WHITE+CLI_INPUT);
            userInput=consoleScanner.next().toUpperCase();
            validInput=true;
            if(!availableCards.contains(userInput))
                validInput=false;
        }
        clientController.setPlayerCardRequest(clientController.getPlayerNickname(), userInput);
        System.out.print(ANSI_LIME+WAIT_PLAYERS+NEW_LINE);
        printedLinesCounter+=3;
    }

    /**
     * Allows the client to setup the network parameters
     * @param clientController is the controller of the client
     */
    @Override
    public void setupConnection(ClientController clientController) {
        //Local Variables
        Scanner consoleScanner = new Scanner(System.in);
        String userInputString;
        int userInputValue = 0;
        boolean isOperationValid = false;
        //Lobby Parameters
        String chosenNickname;
        int chosenLobbySize;
        /*  # TITLE BOX #
            ┌──────────────────┐
            │ Setup Connection │
            └──────────────────┘
        */
        renderTitleBox(ANSI_PURPLE,SETUPTITLE);
        while (!isOperationValid){
            /*  # Setup Server IP #
                Server IP 🌍
                >
                |
            */
            System.out.print(COLORMODE+SERVER_IP+NEW_LINE+CLI_INPUT);
            userInputString=consoleScanner.next();
            printedLinesCounter+=2;
            while (!clientController.getSocketConnection().setServerName(userInputString)){
                /*  # Setup Server IP #
                    Invalid IP...retry • Server IP 🌍
                    >
                    |
                */
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_IP+COLORMODE+SERVER_IP+NEW_LINE+CLI_INPUT);
                userInputString=consoleScanner.next();
            }
            /*  # Setup Socket Port #
                Server IP 🌍
                >
                Socket Port ⛩
                We suggest you the port 1337 • >
                Handshaking with 192.168.1.9 on port 1337...🦖
                |
            */
            System.out.print(COLORMODE+SOCKET_PORT+NEW_LINE+ANSI_GRAY);
            System.out.print(PORT_SUGGESTION+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+PORT_SUGGESTION_ERROR+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            userInputValue=consoleScanner.nextInt();
            printedLinesCounter+=3;
            clientController.getSocketConnection().setServerPort(userInputValue);
            System.out.print(String.format(HANDSHAKING,clientController.getSocketConnection().getServerName(),clientController.getSocketConnection().getServerPort())+NEW_LINE);
            if(clientController.getSocketConnection().startConnection()){
                /*  # Connection Success #
                    Server IP 🌍
                    >
                    Socket Port ⛩
                    We suggest you the port 1337 • >
                    Handshaking with 192.168.1.9 on port 1337...🦖
                    Connection established!
                */
                isOperationValid=true;
                System.out.print(ANSI_GREEN+SUCCESS_HANDSHAKING+ANSI_WHITE+NEW_LINE);
                printedLinesCounter+=1;
            }
            else{
                /*  # Failed Connection #
                    Failed Connection...retry!
                    Server IP 🌍
                    >
                */
                System.out.print(String.format(CURSOR_UP,printedLinesCounter-3));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+FAILED_CONNECTION+NEW_LINE+ANSI_WHITE);
                printedLinesCounter=4;
            }
        }
        /*  # Nickname Setup #
            Server IP 🌍
            >
            Socket Port ⛩
            We suggest you the port 1337 • >
            Handshaking with 192.168.1.9 on port 1337...🦖
            Connection established!
            Nickname 👾
            >
            |
        */
        System.out.print(COLORMODE+NICKNAME+NEW_LINE+CLI_INPUT);
        userInputString=consoleScanner.next();
        clientController.setPlayerNickname(userInputString);
        printedLinesCounter+=2;
        /*  # Nickname Setup #
            Server IP 🌍
            >
            Socket Port ⛩
            We suggest you the port 1337 • >
            Handshaking with 192.168.1.9 on port 1337...🦖
            Connection established!
            Nickname 👾
            >
            Lobby Size 📦
            >
            |
        */
        System.out.print(LOBBY_SIZE+NEW_LINE+CLI_INPUT);
        while(!consoleScanner.hasNextInt()){
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.print(ANSI_RED+INVALID_LOBBY_SIZE+ANSI_WHITE+CLI_INPUT);
            consoleScanner.next();
        }
        userInputValue=consoleScanner.nextInt();
        printedLinesCounter+=2;
        //Only two valid choices...
        isOperationValid=false;
        if(userInputValue==2 || userInputValue == 3)
            isOperationValid=true;
        while (!isOperationValid){
            /*  # Nickname Setup #
                Server IP 🌍
                >
                Socket Port ⛩
                We suggest you the port 1337 • >
                Handshaking with 192.168.1.9 on port 1337...🦖
                Connection established!
                Nickname 👾
                >
                This game is just for 2 or 3 people...retry! • Lobby Size 📦
                >
                |
            */
            System.out.print(String.format(CURSOR_UP,2));
            System.out.print(CLEAN);
            System.out.print(ANSI_RED+LOBBY_SIZE_ERROR+COLORMODE+LOBBY_SIZE+NEW_LINE+CLI_INPUT);
            userInputValue=consoleScanner.nextInt();
            isOperationValid=false;
            if(userInputValue==2 || userInputValue == 3)
                isOperationValid=true;
        }
        //Save user preferences
        chosenLobbySize=userInputValue;
        chosenNickname=clientController.getPlayerNickname();
        //Adds the player to the lobby
        clientController.addPlayerRequest(chosenNickname,userInputValue);
        System.out.print(LOBBY_JOIN+NEW_LINE);
        printedLinesCounter+=1;
        //Troubles with the lobby...
        //# Full Lobby # -> we close the client
        if(clientController.isFullLobby()){
            System.out.println(ANSI_RED+UNAVAILABLE_LOBBY+NEW_LINE+ANSI_WHITE+"Closing the program...");
            System.exit(0);
        }
        while(!clientController.getValidNick()){
            /*  # Nickname Unavailable # -> There is a player with the same nickname in the lobby
                Server IP 🌍
                >
                Socket Port ⛩
                We suggest you the port 1337 • >
                Handshaking with 192.168.1.9 on port 1337...🦖
                Connection established!
                Nickname 👾
                >
                Lobby Size 📦
                >
                Joining the lobby...
                There is already a player with this nickname in the lobby...retry!
                > |
            */
            System.out.println(ANSI_RED+NICKNAME_ERROR);
            System.out.print(ANSI_WHITE+CLI_INPUT);
            userInputString=consoleScanner.next();
            clientController.setPlayerNickname(userInputString);
            clientController.addPlayerRequest(clientController.getPlayerNickname(),chosenLobbySize);
            System.out.print(String.format(CURSOR_UP,2));
            System.out.print(CLEAN);
        }
        /*  # Setup Done! #
            Server IP 🌍
            >
            Socket Port ⛩
            We suggest you the port 1337 • >
            Handshaking with 192.168.1.9 on port 1337...🦖
            Connection established!
            Nickname 👾
            >
            Lobby Size 📦
            >
            Joining the lobby...
            You have correctly joined the lobby!
            > Wait for the match startup...
            |
        */
        System.out.print(ANSI_GREEN+SUCCESS_LOBBY_ACCESS+NEW_LINE);
        System.out.print(COLORMODE+CLI_INPUT+WAITSTART+NEW_LINE);
        printedLinesCounter+=2;
    }

    //TODO: Polish the code below this todo

    /**
     * Renders the players information box
     *      PLAYERS
     *      ┌────────────────────┐
     *      │ ◈ SteveJobs|ATHENA │
     *      │ ...                │
     *      └────────────────────┘
     */
    public void renderPlayersInfoBox(ClientController clientController){
        int maxLength = 0;
        String pieceOfString;
        List<String> playerInfo = new ArrayList<>();
        StringBuilder playerData = new StringBuilder();
        System.out.print(COLORMODE+PLAYERS_TITLE+NEW_LINE);
        //Build the players info strings
        for(PlayerInterface current : clientController.getPlayers()){
            pieceOfString= WorkerColorsMap.get(current.getColor());
            playerData.append(V_THIN_LINE + BLANK).append(pieceOfString);
            pieceOfString = current.getPlayerNickname();
            playerData.append(BLANK).append(COLORMODE).append(pieceOfString);
            pieceOfString = current.getCard();
            playerData.append(BLANK+"•"+BLANK).append(pieceOfString).append(BLANK+V_THIN_LINE);
            playerInfo.add(playerData.toString());}
        //Discover the longest string
        for(String current : playerInfo){
            if(current.length()>maxLength)
                maxLength=current.length();}
        //Print the box
        System.out.print(BLANK+BLANK+BLANK+COLORMODE+PLAYERS_TITLE+NEW_LINE);
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
     *            BOARD                              0
     *      0   1   2   3   4      FULL TOWERS       1
     *    ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐             2
     *  0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 4 ┊             3
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘             4
     *  1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE     5
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌┐      6
     *  2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Building ┊      7
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌┘      8
     *  3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES   9
     *    ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐   10
     *  4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ [2|1] [0|2] ┊   11
     *    ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌┘   12
     *                                               13
     * > Type a command...                           14
     */
    public void renderCLI(String phase, String listOfMoves){
        //Clean last graphic elements
        System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        System.out.print(CLEAN);
        //Print line 0
        for(int i=0;i<boardTitleEdgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(ANSI_WHITE+BOARD_TITLE+NEW_LINE);
        //Print line 1
        for(int i=0;i<horizontalRowNumberDistance;i++)
            System.out.print(BLANK);
        System.out.print(HORIZONTAL_ROW_NUMBER);
        for(int i=0;i<horizontalRowNumberDistance;i++)
            System.out.print(BLANK);
        System.out.print(ANSI_LIGHTBLUE+TOWERS_TITLE+NEW_LINE+ANSI_WHITE);
        //Print line 2
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(upperEdgeBoard);
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(ANSI_LIGHTBLUE+upperEdgeTowers+ANSI_WHITE+NEW_LINE);
        //Print line 3
        //Print line 4
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(intermediateEdgeBoard);
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(ANSI_LIGHTBLUE+lowerEdgeTowers+ANSI_WHITE+NEW_LINE);
        //Print line 5
        //Print line 6
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(intermediateEdgeBoard);
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        renderGameInfoBoxUpperEdge(ANSI_RED,currentPhase);
        System.out.print(ANSI_WHITE+NEW_LINE);
        //Print line 7
        //Print line 8
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(intermediateEdgeBoard);
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        renderGameInfoBoxLowerEdge(ANSI_RED,currentPhase);
        System.out.print(ANSI_WHITE+NEW_LINE);
        //Print line 9
        //Print line 10
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(intermediateEdgeBoard);
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        renderGameInfoBoxUpperEdge(ANSI_GREEN,listOfMoves);
        System.out.print(ANSI_WHITE+NEW_LINE);
        //Print line 11
        //Print line 12
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        System.out.print(intermediateEdgeBoard);
        for(int i=0;i<edgeDistance;i++)
            System.out.print(BLANK);
        renderGameInfoBoxLowerEdge(ANSI_RED,listOfMoves);
        System.out.print(ANSI_GREEN+NEW_LINE);
        //Print line 13
        System.out.print(NEW_LINE);
        //Print line 14
        System.out.print(CLI_INPUT);
        printedLinesCounter=refreshable_area_height;
    }

    //UI ACTION METHODS

    @Override
    public void placeWorkers(ClientController clientController) {

    }

    @Override
    public void selectWorker(ClientController clientController) {

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
}
