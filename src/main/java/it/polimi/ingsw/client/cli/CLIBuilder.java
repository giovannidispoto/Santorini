package it.polimi.ingsw.client.cli;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.clientModel.basic.Step;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.SantoriniException;
import it.polimi.ingsw.client.controller.UIActions;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.PlayerInterface;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase.WorkerPositionInterface;

import java.util.*;
import java.util.List;

/**
 * CLIBuilder contains everything you need to build the CLI and use it
 */
public class CLIBuilder implements UIActions{

    //------------------ # ANSI Colors 256 bit # ------------------
    private static final String CODE_BLUE = "33";
    private static final String CODE_LIGHTBLUE = "75";
    private static final String CODE_GRAY = "244";
    private static final String CODE_LIGHT_GRAY = "248";
    private static final String CODE_GREEN = "41";
    private static final String CODE_LIGHT_GREEN = "83";
    private static final String CODE_BROWN = "130";
    private static final String CODE_RED = "197";
    private static final String CODE_PURPLE = "99";
    private static final String CODE_ORANGE = "215";
    private static final String CODE_BLACK = "232";
    private static final String CODE_WHITE = "255";
    private static final String ANSI_PREFIX = "\u001b[38;5;";

    private static final String ANSI_BLUE = ANSI_PREFIX+CODE_BLUE+"m";
    private static final String ANSI_LIGHTBLUE = ANSI_PREFIX+CODE_LIGHTBLUE+"m";
    private static final String ANSI_GRAY = ANSI_PREFIX+CODE_GRAY+"m";
    private static final String ANSI_LIGHT_GRAY = ANSI_PREFIX+CODE_LIGHT_GRAY+"m";
    private static final String ANSI_GREEN = ANSI_PREFIX+CODE_GREEN+"m";
    private static final String ANSI_LIGHT_GREEN = ANSI_PREFIX+CODE_LIGHT_GREEN+"m";
    private static final String ANSI_BROWN = ANSI_PREFIX+CODE_BROWN+"m";
    private static final String ANSI_PURPLE = ANSI_PREFIX+CODE_PURPLE+"m";
    private static final String ANSI_RED = ANSI_PREFIX+CODE_RED+"m";
    private static final String ANSI_ORANGE = ANSI_PREFIX+CODE_ORANGE+"m";
    private static final String ANSI_BLACK = ANSI_PREFIX+CODE_BLACK+"m";
    private static final String ANSI_WHITE = ANSI_PREFIX+CODE_WHITE+"m";
    //Dark Mode function
    private static String COLOR_MODE;
    //Specials
    protected static final String CLEAN = "\u001b[0J";
    //General Purpose
    private static final String NEW_LINE = "\n";
    private static final String BLANK = " ";

    //------------------ # UI Structure # ------------------

    //Board
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

    //Data Boxes
    protected static final String L_THIN_T_CORNER = "┌";
    protected static final String R_THIN_T_CORNER = "┐";
    protected static final String L_THIN_B_CORNER = "└";
    protected static final String R_THIN_B_CORNER = "┘";
    protected static final String H_THIN_LINE = "─";
    protected static final String V_THIN_LINE = "│";

    //------------------ # UI Objects # ------------------
    private static final String CLI_INPUT = "> ";
    private static final String WORKER = "✲";
    protected static final String DOME = ANSI_LIGHTBLUE+"◉"+ANSI_WHITE;
    protected static final String GRASS = ANSI_GREEN+"☘︎"+ANSI_WHITE;
    private static final String SANTORINI = ANSI_LIGHTBLUE+"SANTORINI"+ANSI_WHITE;
    private static final String WELCOME = "Welcome to %s : The Board Game 🎲 → Enable the full screen to enjoy the best experience";
    private static final String BOARD_TITLE = "BOARD";
    private static final String PLAYERS_TITLE = "PLAYERS 👦🏼";
    private static final String TOWERS_TITLE = "FULL TOWERS 🏗";
    private static final String MOVES_TITLE = "PLAYER MOVES 🎮";
    private static final String PHASE_TITLE = "CURRENT PHASE 🚀";

    //------------------ # UI Data # ------------------
    private HashMap<Color,String> WorkerColorsMap;
    private CLIDataObject[] boardCellsContents; // Each element is a row ┃   ┃   ┃   ┃   ┃   ┃
    private List<String> playerMoves; //Available Moves for the Player
    private String currentPhase;
    private int fullTowersNumber;
    private boolean operationRepeated=false;
    private HashMap<Integer,String> phasesMap;

    //------------------ # Titles # ------------------
    private static final String SETUP_TITLE = "Setup Connection";
    private static final String PICK_TITLE = "Cards Pick Up";
    private static final String CHOICE_TITLE = "Card Choice";

    //------------------ # Requests Messages # ------------------

    //Web
    private static final String SOCKET_PORT = "Socket Port ⛩";
    private static final String PORT_SUGGESTION = "We suggest you the port 1337 • ";
    private static final String SERVER_IP = "Server IP 🌍";
    private static final String NICKNAME = "Nickname 👾";
    private static final String LOBBY_SIZE = "Lobby Size 📦";
    private static final String WAIT_START = "Wait for the match startup...";
    private static final String WAIT_PLAYERS = "Wait for the other players choices...";
    private static final String LOBBY_JOIN = "Joining the lobby...";

    //Cards
    private static final String CHOOSE_CARD = "Choose your card for this match 🕹";

    //Worker Placement
    private static final String PLACEMENT_REQUEST = "Place two workers on the board";
    private static final String ROW_WORKER = "Worker row • ";
    private static final String COL_WORKER = "Worker column • ";

    //Worker Selection
    private static final String WORKER_SELECTION_REQUEST = "Select a worker for this turn";
    //Movement
    private static final String MOVEMENT_REQUEST = "Select a valid cell for the movement phase";
    private static final String ROW_CELL = "Cell row • ";
    private static final String COL_CELL = "Cell column • ";
    private static final String SKIP = "Do you want to skip this action? [yes/no] • ";
    private static final String REPEAT = "Do you want to repeat this phase? [yes/no] • ";
    //Building
    private static final String BUILDING_REQUEST = "Select a valid cell for the building phase";
    //Remove
    private static final String REMOVE_REQUEST = "Select a valid tower for the removal phase";
    private static final String TOWER_ROW_CELL = "Tower cell row • ";
    private static final String TOWER_COL_CELL = "Tower cell column • ";
    //Skip
    private static final String SKIP_REQUEST = "Skip request";
    //Repeat
    private static final String REPEAT_REQUEST = "Repeat request";
    //Turn Type
    private static final String TYPE_REQUEST = "Activate God Power";
    private static final String TYPE = "Do you want to use your God Power for this turn? [yes/no] • ";

    //------------------ # Successes Messages # ------------------
    //Web
    private static final String SUCCESSFUL_HANDSHAKING = "Connection established!";
    private static final String SUCCESSFUL_LOBBY_ACCESS = "You have correctly joined the lobby!";

    //Worker Selection
    private static final String SUCCESSFUL_SELECTION = "You have selected the worker in the cell [%s|%s]";

    //------------------ # Failures and Errors Messages # ------------------

    //General Purpose
    private static final String NOT_A_NUMBER = "This is not a number...retry! • ";
    private static final String INVALID_INPUT = "Invalid input...retry! • ";

    //Web
    private static final String INVALID_IP = "Invalid IP...retry! • ";
    private static final String UNAVAILABLE_LOBBY = "The selected lobby is full... try later 😭";
    private static final String ALREADY_EXISTENT_LOBBY = "Someone else already have created a lobby...choose the other one!";
    private static final String NICKNAME_ERROR = "There is already a player with this nickname in this server...retry!";
    private static final String FAILED_CONNECTION = "Troubles with the connection...retry!";
    private static final String LOBBY_SIZE_ERROR = "This game is just for 2 or 3 people...retry! • ";
    private static final String PORT_SUGGESTION_ERROR = "This is not a number! We suggest you the port 1337 • ";

    //Cards
    private static final String INVALID_CARD = "Invalid card choice...retry! • ";

    //Fatal
    private static final String FATAL_ERROR = "Something broke down! • PROBLEM: %s 🔥";
    private static final String EXIT = "Type [quit] to close the program • ";
    private static final String GOODBYE = "Goodbye...hope to see you soon 😪";
    private static final String CLOSING = "Closing the program...";

    //Worker Placement
    private static final String OCCUPIED_POSITION = "Already occupied cell...retry! • ";
    private static final String INVALID_COORDINATE = "Invalid coordinate... [0 and 4] • ";

    //Worker Selection
    private static final String INVALID_SELECTION = "Invalid selection... retry! • ";
    private static final String INVALID_WORKER = "This worker is blocked...choose the other one! • ";

    //Movement
    private static final String INVALID_CELL = "Invalid cell... retry! • ";

    //Remove
    private static final String INVALID_TOWER_CELL = "Invalid tower cell... retry! • ";

    //------------------ # Templates # ------------------

    //Cursor
    protected static final String CURSOR_UP = "\u001b[%sA";

    //Web
    private static final String handshakingTemplate = "Handshaking with %s on port %s...🦖 ";
    private static final String godChoiceTemplate = "%s is picking up the cards for this match...";

    //Moves
    private static final String playerMoveTemplate = "[%s|%s]";

    //Data Box
    private static final String playerDataTemplate = "%s %s | %s";

    //Cards
    private static final String pickChoiceTemplate = "Your %s choice • ";
    private static final String cardTemplate = "• %s | %s";
    private static final String waitGodTemplate = "Wait while %s chooses the cards for this match...";
    private static final String pickCardsTemplate = "You're the player chosen by the gods! Choose %s cards for this match 👑";
    //Turn
    private static final String waitTurn = "Wait till %s ends his turn...";
    private static final String waitGeneric = "Wait...";
    //Board
    private static final String horizontalNumberRowTemplate = BLANK+BLANK+"%s"+BLANK;

    //Board Preconfigured Elements
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

    //Full Towers Box Preconfigured Elements
    private static final String upperEdgeTowers = L_THIN_T_CORNER+H_THIN_LINE+H_THIN_LINE+H_THIN_LINE+R_THIN_T_CORNER;
    private static final String intermediateEdgeTowers = V_THIN_LINE+BLANK+"%s"+BLANK+V_THIN_LINE;
    private static final String lowerEdgeTowers = L_THIN_B_CORNER+H_THIN_LINE+H_THIN_LINE+H_THIN_LINE+R_THIN_B_CORNER;

    //Current Phase Box Preconfigured Elements
    private static final String intermediateEdgePhase = V_THIN_LINE+BLANK+"%s"+BLANK+V_THIN_LINE;

    //Sizes
    private int godPlayerRefreshHeight;
    private int printedLinesCounter;
    private final int boardTitleEdgeDistance = 12;
    private int refreshableAreaHeight = 17;
    private final int editableRowCells = 5;


    private Scanner consoleScanner;

    //DONE: Class Constructor
    /**
     * Class Constructor
     */
    public CLIBuilder(String colorMode, ClientController clientController, Scanner consoleScanner) {
        this.consoleScanner = consoleScanner;
        this.boardCellsContents = new CLIDataObject[5];
        this.playerMoves = new ArrayList<>();
        this.WorkerColorsMap = new HashMap<>();
        this.phasesMap = new HashMap<>();
        this.fullTowersNumber = 0;
        this.godPlayerRefreshHeight=0;
        this.printedLinesCounter = 0;
        //Color Scheme setup
        if(colorMode.equalsIgnoreCase("light"))
            COLOR_MODE=ANSI_BLACK;
        else
            COLOR_MODE=ANSI_WHITE;
        //CLI Board Representation Setup
        for(int i=0;i<editableRowCells;i++){
            CLIDataObject cell = new CLIDataObject();
            boardCellsContents[i]=cell;
        }
        //Fulfill the WorkerColorsMap
        WorkerColorsMap.put(Color.BLUE,ANSI_BLUE+WORKER);
        WorkerColorsMap.put(Color.BROWN,ANSI_BROWN+WORKER);
        WorkerColorsMap.put(Color.GREY, ANSI_GRAY +WORKER);
        phasesMap.put(0,"Placement");
        phasesMap.put(1,"Selection");
        phasesMap.put(2,"Movement");
        phasesMap.put(3,"Building");
        phasesMap.put(4,"Removal");
        phasesMap.put(5,"Type");
    }

    //SECTION: Support Methods

    /**
     * Prints notification about God player activity
     */
    public void printGodPlayerActivity(ClientController clientController){
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.println(ANSI_WHITE+CLI_INPUT+ANSI_ORANGE+String.format(waitGodTemplate,clientController.getGodPlayer())+ANSI_WHITE);
    }

    /**
     * Extracts from the worker view a string which represents all the available cells
     * @param clientController is the client-side controller
     * @return String obj
     */
    public String decomposeWorkerView(ClientController clientController){
        //Local Variables
        StringBuilder movesBuilder = new StringBuilder();
        //Logic
        for(int row=0;row<5;row++)
            for (int col=0;col<5;col++){
                //Add a blank space between to moves, except for the last one
                if(clientController.getWorkerViewCell(row,col))
                    movesBuilder.append(String.format(playerMoveTemplate,row,col)).append(BLANK);
            }
        return movesBuilder.toString();
    }

    /**
     * Transforms each Battlefield cell in a formatted String
     * @param battlefield is the Battlefield client-side class (a singleton)
     */
    public void writeBattlefieldData(BattlefieldClient battlefield){
        //Transform cells into strings
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                boardCellsContents[i].writeCellData(j,battlefield.getCell(i,j));
            }
        }
        //Saves the number of full towers
        fullTowersNumber=battlefield.countFullTowers();
    }

    /**
     * Prints players information
     * @param clientController is the client-side controller
     */
    public void renderPlayersInfo(ClientController clientController) throws SantoriniException {
        //Local Variables
        StringBuilder playerData = new StringBuilder();
        List<String> playersInfo = new ArrayList<>();
        //Request
        clientController.getPlayersRequest();
        //Logic
        System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+edge_distance+PLAYERS_TITLE+NEW_LINE+NEW_LINE);
        for(PlayerInterface currentPlayer : clientController.getPlayers()){
            playerData.append(edge_distance).append(String.format(playerDataTemplate, WorkerColorsMap.get(currentPlayer.getColor()), ANSI_WHITE+currentPlayer.getPlayerNickname(), currentPlayer.getCard()));
            playersInfo.add(playerData.toString());
            playerData.setLength(0);
        }
        for(String current : playersInfo)
            System.out.println(current);
        System.out.print(NEW_LINE);
        System.out.println(edge_distance+GRASS+BLANK+"is the grass, a good place to build a tower");
        System.out.println(edge_distance+DOME+BLANK+"is a dome, the highest level of a tower ");
        System.out.print(NEW_LINE+NEW_LINE);
        printedLinesCounter=0;
    }

    /**
     * Renders a generic message box
     * @param messageColor is the chosen color for the box
     * @param message is the string that will be printed in the box
     */
    public void renderTitleBox(String messageColor, String message){
        int messageLength = message.length();
        System.out.print(messageColor+L_THIN_T_CORNER);
        //+2 to consider the blank spaces between the message and the lateral edges -> │ message │
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
     *  Renders the entire deck (list of cards)
     *  • CHRONUS | effect...
     *  ...
     *  • ZEUS | effect...
     * @param clientController is the client side controller
     */
    public void renderDeck(ClientController clientController) throws SantoriniException {
        clientController.getDeckRequest();
        for(DivinityCard current : clientController.getCardsDeck().getAllCards()){
            System.out.println(String.format(cardTemplate,ANSI_LIGHTBLUE+current.getCardName().toUpperCase()+ANSI_WHITE,current.getCardEffect()));
            printedLinesCounter+=1;
        }
        System.out.print(NEW_LINE);
        printedLinesCounter+=1;
    }

    /**
     *  Renders the available cards for the initial player choice
     *  • CHRONUS | Owner win if there are five full towers on the board
     * @param clientController is the client side controller
     */
    public void renderAvailableCards(ClientController clientController){
        for(String card : clientController.getGodCards()){
            System.out.println(String.format(cardTemplate,ANSI_LIGHTBLUE+clientController.getCardsDeck().getDivinityCard(card).getCardName().toUpperCase()+ANSI_WHITE,clientController.getCardsDeck().getDivinityCard(card).getCardEffect()));
            printedLinesCounter+=1;
        }
        System.out.print(NEW_LINE);
        printedLinesCounter+=1;
    }

    /**
     * Renders the CLI Data part
     * 0 |            BOARD
     * 1 |
     * 2 |     0   1   2   3   4      FULL TOWERS 🏗
     * 3 |   ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐
     * 4 | 0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 4 ┊
     * 5 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘
     * 6 | 1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE 🚀
     * 7 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌┐
     * 8 | 2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Building ┊
     * 9 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌┘
     * 10| 3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES 🎮
     * 11|   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 12| 4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ [2|1] [0|2] ┊
     * 13|   ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 14|
     * 15|> Wait... || Wait till Players ends his turn...
     * 16|
     * @param availableMoves is a formatted String with all the available moves for the player
     *
     */
    public void renderBoard(String availableMoves){
        //Local Variables
        StringBuilder currentLine = new StringBuilder();
        int currentRow = 0;

        //Clean the screen
        System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        System.out.print(CLEAN);
        printedLinesCounter=refreshableAreaHeight;

        //Print line 0
        currentLine.append(BLANK.repeat(boardTitleEdgeDistance));
        currentLine.append(ANSI_WHITE+BOARD_TITLE);
        System.out.print(currentLine+NEW_LINE);
        System.out.print(NEW_LINE);
        currentLine.setLength(0);

        //Print line 2
        currentLine.append(edge_distance);
        for(int i=0;i<5;i++)
            currentLine.append(String.format(horizontalNumberRowTemplate,i));
        currentLine.append(BLANK+edge_distance).append(ANSI_LIGHTBLUE+TOWERS_TITLE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 3
        currentLine.append(edge_distance+upperEdgeBoard+edge_distance+ANSI_LIGHTBLUE+upperEdgeTowers+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 4
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_LIGHTBLUE).append(String.format(intermediateEdgeTowers, fullTowersNumber)).append(ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 5
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance+ANSI_LIGHTBLUE+lowerEdgeTowers+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 6
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_RED+PHASE_TITLE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 7
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance);
        currentLine.append(ANSI_RED+L_THIN_T_CORNER);
        currentLine.append(H_THIN_LINE.repeat(Math.max(0, currentPhase.length() + 2)));
        currentLine.append(R_THIN_T_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 8
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_RED).append(String.format(intermediateEdgePhase, currentPhase)).append(ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 9
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance);
        currentLine.append(ANSI_RED+L_THIN_B_CORNER);
        currentLine.append(H_THIN_LINE.repeat(Math.max(0, currentPhase.length() + 2)));
        currentLine.append(R_THIN_B_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 10
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+MOVES_TITLE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 11
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+L_THIN_T_CORNER+H_THIN_LINE);
        currentLine.append(H_THIN_LINE.repeat(availableMoves.length()));
        currentLine.append(R_THIN_T_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 12
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+V_THIN_LINE+BLANK);
        currentLine.append(availableMoves);
        currentLine.append(V_THIN_LINE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 13
        currentLine.append(edge_distance+lowerEdgeBoard+edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+L_THIN_B_CORNER+H_THIN_LINE);
        currentLine.append(H_THIN_LINE.repeat(availableMoves.length()));
        currentLine.append(R_THIN_B_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 14 and 15
        System.out.print(NEW_LINE+NEW_LINE);

        System.out.print(ANSI_WHITE+CLI_INPUT+ANSI_ORANGE+waitGeneric+NEW_LINE);
    }

    //SECTION: UI Methods

    /**
     * Allows the user to register himself to the server
     * @param clientController is the client-side controller
     */
    @Override
    public void setupConnection(ClientController clientController) throws SantoriniException {
        //Local Variables
        String userInputString;
        int userInputValue;
        boolean isOperationValid = false;
        //Lobby Parameters
        String chosenNickname;
        int chosenLobbySize;
        System.out.print(NEW_LINE+COLOR_MODE+String.format(WELCOME,COLOR_MODE+SANTORINI)+NEW_LINE+NEW_LINE);
        printedLinesCounter+=2;
        /*  # TITLE BOX #
            ┌──────────────────┐
            │ Setup Connection │
            └──────────────────┘
        */
        renderTitleBox(ANSI_PURPLE,SETUP_TITLE);
        while (!isOperationValid){
            /*  # Setup Server IP #
                Server IP 🌍
                >
                |
            */
            System.out.print(COLOR_MODE+SERVER_IP+NEW_LINE+CLI_INPUT);
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
                System.out.print(ANSI_RED+INVALID_IP+COLOR_MODE+SERVER_IP+NEW_LINE+CLI_INPUT);
                userInputString=consoleScanner.next();
            }
            /*  # Setup Socket Port #
                Server IP 🌍
                >
                Socket Port ⛩
                We suggest you the port 1337 • >
                |
            */
            System.out.print(COLOR_MODE+SOCKET_PORT+NEW_LINE+ ANSI_GRAY);
            System.out.print(PORT_SUGGESTION+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+PORT_SUGGESTION_ERROR+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            userInputValue=consoleScanner.nextInt();
            printedLinesCounter+=2;
            clientController.getSocketConnection().setServerPort(userInputValue);
                /*  # Connection Success #
                    Server IP 🌍
                    >
                    Socket Port ⛩
                    We suggest you the port 1337 • >
                    Handshaking with 192.168.1.9 on port 1337...🦖
                    |
                */
            System.out.print(String.format(handshakingTemplate,clientController.getSocketConnection().getServerName(),clientController.getSocketConnection().getServerPort())+NEW_LINE);
            printedLinesCounter+=1;
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
                System.out.print(ANSI_GREEN+SUCCESSFUL_HANDSHAKING+ANSI_WHITE+NEW_LINE);
                printedLinesCounter+=1;
            }
            else{
                /*  # Failed Connection #
                    Failed Connection...retry!
                    Server IP 🌍
                    >
                */
                System.out.print(String.format(CURSOR_UP,printedLinesCounter-6)); //-3 because we don't have to delete the title box and the welcome message
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+FAILED_CONNECTION+NEW_LINE+ANSI_WHITE);
                printedLinesCounter=6;
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
        System.out.print(COLOR_MODE+NICKNAME+NEW_LINE+CLI_INPUT);
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
            System.out.print(ANSI_RED+LOBBY_SIZE_ERROR+ANSI_WHITE+CLI_INPUT);
            consoleScanner.next();
        }
        userInputValue=consoleScanner.nextInt();
        printedLinesCounter+=2;
        //Only two valid choices...
        isOperationValid= userInputValue == 2 || userInputValue == 3;
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
            System.out.print(ANSI_RED+LOBBY_SIZE_ERROR+COLOR_MODE+LOBBY_SIZE+NEW_LINE+CLI_INPUT);
            userInputValue=consoleScanner.nextInt();
            isOperationValid= userInputValue == 2 || userInputValue == 3;
        }
        //Save user preferences
        chosenLobbySize=userInputValue;
        chosenNickname=clientController.getPlayerNickname();
        //Adds the player to the lobby
        clientController.addPlayerRequest(chosenNickname,userInputValue);
        System.out.print(LOBBY_JOIN+NEW_LINE);
        printedLinesCounter+=1;
        //Troubles with the lobby...
        while(!clientController.getLobbyState() && !clientController.isFullLobby()){
            /*  # Already Existent Lobby # -> Someone else have already created a lobby
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
                Someone else already have created a lobby...choose the other one!
                > |
            */
            System.out.println(ANSI_RED+ALREADY_EXISTENT_LOBBY);
            System.out.print(ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+LOBBY_SIZE_ERROR+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            userInputValue=consoleScanner.nextInt();
            chosenLobbySize=userInputValue;
            clientController.addPlayerRequest(clientController.getPlayerNickname(),chosenLobbySize);
            System.out.print(String.format(CURSOR_UP,2));
            System.out.print(CLEAN);
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
                There is already a player with this nickname in this server...retry!
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
        //# Full Lobby # -> we close the client
        if(clientController.isFullLobby()){
            System.out.println(ANSI_RED+UNAVAILABLE_LOBBY);
            System.out.print(ANSI_GRAY+EXIT+ANSI_WHITE+CLI_INPUT);
            userInputString=consoleScanner.next();
            while (!userInputString.equalsIgnoreCase("quit")){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.println(CLEAN);
                System.out.print(ANSI_RED+INVALID_INPUT+ANSI_GRAY+EXIT+ANSI_WHITE+CLI_INPUT);
                userInputString=consoleScanner.next();
            }
            System.out.println(GOODBYE+NEW_LINE+CLOSING);
            System.exit(0);
        }
        /*  # Setup Completed! #
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
        System.out.print(ANSI_GREEN+ SUCCESSFUL_LOBBY_ACCESS +NEW_LINE);
        System.out.print(COLOR_MODE+CLI_INPUT+ANSI_GRAY+WAIT_START+ANSI_WHITE+NEW_LINE);
        printedLinesCounter+=2;
    }

    /**
     * Allows the god player to pick up the cards for the match
     * @param clientController is the client-side controller
     */
    @Override
    public void pickCards(ClientController clientController) throws SantoriniException {
        //Local Variables
        boolean isValidInput;
        int numberOfPlayers=clientController.getCurrentLobbySize();
        int pickedCounter=0;
        String userInput;
        List<String> chosenCards = new ArrayList<>();
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

            You're the player chosen by the gods! Choose %s cards for this match 👑
            Your 1 choice • > |
         */
        printedLinesCounter=0;
        renderTitleBox(ANSI_PURPLE,PICK_TITLE);
        renderDeck(clientController);
        System.out.print(String.format(pickCardsTemplate,numberOfPlayers)+NEW_LINE);
        //Multiple extraction
        while (pickedCounter<numberOfPlayers){
            System.out.print(ANSI_GRAY+String.format(pickChoiceTemplate,pickedCounter+1)+ANSI_WHITE+CLI_INPUT);
            userInput=consoleScanner.next().toUpperCase();
            isValidInput= clientController.getCardsDeck().getCardsNames().contains(userInput);
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

            You're the player chosen by the gods! Choose %s cards for this match 👑
            > Wait for other players choice...
            |
         */
        System.out.print(ANSI_WHITE+CLI_INPUT+ANSI_ORANGE+WAIT_PLAYERS+NEW_LINE);
        printedLinesCounter+=2;
        godPlayerRefreshHeight=printedLinesCounter;
    }

    /**
     * Allows the user to choose his card for the match
     * @param clientController is the client-side controller
     */
    @Override
    public void chooseCard(ClientController clientController) {
        //Local Variables
        boolean validInput = true;
        String userInput;
        List<String> availableCards = clientController.getGodCards();
        //Clean the CLI from the last phase elements
        if(clientController.getGodPlayer().equals(clientController.getPlayerNickname())){
            System.out.print(String.format(CURSOR_UP,godPlayerRefreshHeight));
        }
        else{
            System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        }
        System.out.print(CLEAN);
        //Render the graphic elements
        /*   # Card Selection #
            ┌─────────────┐
            │ Card Choice │
            └─────────────┘
            •
            •
            • ZEUS | Your Build: Your Worker may build a block under itself

            Choose your card for this match 🕹
            > Wait for other players choice...

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
            validInput= availableCards.contains(userInput);
        }
        clientController.setPlayerCardRequest(clientController.getPlayerNickname(), userInput);
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(ANSI_WHITE+CLI_INPUT+ANSI_ORANGE+WAIT_PLAYERS+NEW_LINE);
        printedLinesCounter+=2;
    }

    /**
     * Allows the user to place his workers on the board
     * 0 |            BOARD
     * 1 |
     * 2 |     0   1   2   3   4      FULL TOWERS 🏗
     * 3 |   ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐
     * 4 | 0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 0 ┊
     * 5 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘
     * 6 | 1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE 🚀
     * 7 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌┐
     * 8 | 2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Building ┊
     * 9 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌┘
     * 10| 3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES 🎮
     * 11|   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 12| 4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Choose a free cell ┊
     * 13|   ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 14|
     * 15|Place two workers on the board
     * 16|Worker row • >
     * 17|
     * @param clientController is the client-side controller
     */
    @Override
    public WorkerPositionInterface placeWorkers(ClientController clientController, int workerID) {
        //Local Variables
        String userInput;
        List<String> placementMoves = new ArrayList<>();
        boolean repeat;
        int workerRow,workerCol;
        currentPhase = phasesMap.get(0);
        writeBattlefieldData(BattlefieldClient.getBattlefieldInstance());
        renderBoard("Choose a free cell"+BLANK);
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+PLACEMENT_REQUEST+NEW_LINE);
        //Logic
        do {
            /* # ROW COORDINATE #
                * 14|
                * 15|Place two workers on the board
                * 16|Worker row • > []
                * 17|
             */
            System.out.print(ANSI_GRAY+ROW_WORKER+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            workerRow = consoleScanner.nextInt();
            while (workerRow<0 || workerRow>4){
                /* # ROW COORDINATE ERROR #
                    * 14|
                    * 15|Place two workers on the board
                    * 16|Invalid coordinate...stay between 0 and 4! • Worker row • > []
                    * 17|
                 */
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+ROW_WORKER+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                workerRow = consoleScanner.nextInt();
            }
            /* # COL COORDINATE #
             * 14|
             * 15|Place two workers on the board
             * 16|Worker col • > []
             * 17|
             */
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.print(ANSI_GRAY+COL_WORKER+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                /* # COL COORDINATE ERROR #
                 * 14|
                 * 15|Place two workers on the board
                 * 16|Invalid coordinate...stay between 0 and 4! • Worker col • > []
                 * 17|
                 */
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            workerCol = consoleScanner.nextInt();
            while (workerCol<0 || workerCol>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                workerCol = consoleScanner.nextInt();
            }
            if(BattlefieldClient.getBattlefieldInstance().isCellOccupied(workerRow,workerCol)){
                /* # INVALID COORDINATES #
                 * 14|
                 * 15|Already occupied position...retry! • Place two workers on the board
                 * 16|
                 * 17|
                 */
                repeat = true;
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+OCCUPIED_POSITION+ANSI_WHITE+PLACEMENT_REQUEST);
            }
            else
                repeat = false;
        }while(repeat);
        printedLinesCounter+=1;
        BattlefieldClient.getBattlefieldInstance().getCell(workerRow,workerCol).setPlayer(clientController.getPlayerNickname());
        BattlefieldClient.getBattlefieldInstance().getCell(workerRow,workerCol).setWorkerColor(clientController.getPlayerColor());
        return new WorkerPositionInterface(workerID, workerRow, workerCol);
    }

    /**
     * Allows the user to move a worker
     * 0 |            BOARD
     * 1 |
     * 2 |     0   1   2   3   4      FULL TOWERS 🏗
     * 3 |   ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐
     * 4 | 0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 0 ┊
     * 5 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘
     * 6 | 1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE 🚀
     * 7 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌┐
     * 8 | 2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Placement ┊
     * 9 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌╌┘
     * 10| 3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES 🎮
     * 11|   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 12| 4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Select one of your workers ┊
     * 13|   ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 14|
     * 15|Place two workers on the board
     * 16|Worker row • >
     * 17|
     * @param clientController is the client-side controller
     * @throws SantoriniException is a generic purpose exception
     */
    @Override
    public void selectWorker(ClientController clientController) throws SantoriniException {
        //Local Variables
        int workerRow,workerCol;
        boolean validSelection=false;
        boolean validWorker = false;
        currentPhase=phasesMap.get(1);
        //Clean the previous prompt message
        renderBoard("Select one of your workers"+BLANK);
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        //Logic
        System.out.print(ANSI_WHITE+WORKER_SELECTION_REQUEST+NEW_LINE);
        do{
            do{
                /*  # SELECTION ROW #
                 * 14|
                 * 15|Select a worker for this turn
                 * 16|Worker row • >
                 * 17|
                 */
                System.out.print(ANSI_GRAY+ROW_WORKER+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                workerRow = consoleScanner.nextInt();
                while(workerRow<0 || workerRow>4){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+ROW_WORKER+ANSI_WHITE+CLI_INPUT);
                    while(!consoleScanner.hasNextInt()){
                        System.out.print(String.format(CURSOR_UP,1));
                        System.out.print(CLEAN);
                        System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                        consoleScanner.next();
                    }
                    workerRow = consoleScanner.nextInt();
                }
                /*  # SELECTION COL #
                 * 14|
                 * 15|Select a worker for this turn
                 * 16|Worker col • >
                 * 17|
                 */
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_GRAY+COL_WORKER+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                workerCol = consoleScanner.nextInt();
                while(workerCol<0 || workerCol>4){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+ROW_WORKER+ANSI_WHITE+CLI_INPUT);
                    while(!consoleScanner.hasNextInt()){
                        System.out.print(String.format(CURSOR_UP,1));
                        System.out.print(CLEAN);
                        System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                        consoleScanner.next();
                    }
                    workerCol = consoleScanner.nextInt();
                }

                if(BattlefieldClient.getBattlefieldInstance().getCell(workerRow,workerCol).getWorkerColor()!=null){
                    if(BattlefieldClient.getBattlefieldInstance().getCell(workerRow,workerCol).getWorkerColor().equals(clientController.getPlayerColor()))
                        validSelection=true;
                }
                else
                {
                    /*  # INVALID SELECTION #
                     * 14|
                     * 15|Invalid selection... retry! • Select a worker for this turn
                     * 16|
                     * 17|
                     */
                    System.out.print(String.format(CURSOR_UP,2));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+INVALID_SELECTION+ANSI_WHITE+WORKER_SELECTION_REQUEST+NEW_LINE);
                }
            }
            while (!validSelection);
            printedLinesCounter+=1;
            clientController.selectWorkerRequest(clientController.getPlayerNickname(),workerRow,workerCol);
            if(!clientController.isInvalidWorkerView())
                validWorker=true;
            else
            {
                System.out.print(String.format(CURSOR_UP,printedLinesCounter));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_WORKER+ANSI_WHITE+WORKER_SELECTION_REQUEST+NEW_LINE);
            }
        }
        while(!validWorker);
    }

    /**
     * Move a worker
     * 0 |            BOARD
     * 1 |
     * 2 |     0   1   2   3   4      FULL TOWERS 🏗
     * 3 |   ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐
     * 4 | 0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 4 ┊
     * 5 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘
     * 6 | 1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE 🚀
     * 7 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌┐
     * 8 | 2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Movement ┊
     * 9 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌┘
     * 10| 3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES 🎮
     * 11|   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 12| 4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ [2|1] [0|2] ┊
     * 13|   ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 14|
     * 15|Select a valid cell for the movement phase
     * 16|Cell row • >
     * 17|
     * @param clientController is the client-side controller
     * @throws SantoriniException is a generic purpose exception
     */
    @Override
    public void moveWorker(ClientController clientController) throws SantoriniException {
        //Local Variables
        String userInput;
        boolean skipAvailable = clientController.getCurrentStep().equals(Step.MOVE_SPECIAL);
        boolean skipChosen = false;
        boolean validMove = false;
        int cellRow=0,cellCol=0;
        currentPhase=phasesMap.get(2);
        renderBoard(decomposeWorkerView(clientController));
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+MOVEMENT_REQUEST+NEW_LINE);
        //Logic
        do{
                /*  # MOVEMENT CELL ROW #
                    14|
                    15|Select a valid cell for the movement phase
                    16|Cell row • >
                    17|
                 */
            System.out.print(ANSI_GRAY+ROW_CELL+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            cellRow = consoleScanner.nextInt();
            while(cellRow<0 || cellRow>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+ROW_CELL+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                cellRow = consoleScanner.nextInt();
            }
                /*  # MOVEMENT CELL COL #
                    14|
                    15|Select a valid cell for the movement phase
                    16|Cell col • >
                    17|
                 */
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.print(ANSI_GRAY+COL_CELL+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            cellCol = consoleScanner.nextInt();
            while(cellCol<0 || cellCol>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+COL_CELL+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                cellCol = consoleScanner.nextInt();
            }
            //Check if the inserted values belong to the worker view
            if(clientController.getWorkerViewCell(cellRow,cellCol))
                validMove=true;
            if(!validMove){
                    /*  # MOVEMENT INVALID CELL #
                        14|
                        15|Invalid cell... retry! • Select a valid cell for the movement phase
                        16|
                        17|
                    */
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_CELL+ANSI_WHITE+MOVEMENT_REQUEST+NEW_LINE);
            }

        }while(!validMove);
        printedLinesCounter+=1;
        clientController.playStepRequest(cellRow,cellCol);
    }

    /**
     * Build a block
     * 0 |            BOARD
     * 1 |
     * 2 |     0   1   2   3   4      FULL TOWERS 🏗
     * 3 |   ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐
     * 4 | 0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 4 ┊
     * 5 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘
     * 6 | 1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE 🚀
     * 7 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 8 | 2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Loop Movement ┊
     * 9 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 10| 3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES 🎮
     * 11|   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 12| 4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ [2|1] [0|2] ┊
     * 13|   ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 14|
     * 15|Select a valid cell for the building phase
     * 16|Cell row • >
     * 17|
     * @param clientController is the client-side controller
     * @throws SantoriniException is a generic purpose exception
     */
    @Override
    public void buildBlock(ClientController clientController) throws SantoriniException {
        //Local Variables
        boolean validMove=false;
        int cellRow=0,cellCol=0;
        currentPhase=phasesMap.get(3);
        renderBoard(decomposeWorkerView(clientController));
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+BUILDING_REQUEST+NEW_LINE);
        //Logic
        do{
                /*  # BUILDING CELL ROW #
                    14|
                    15|Select a valid cell for the building phase
                    16|Cell row • >
                    17|
                 */
            System.out.print(ANSI_GRAY+ROW_CELL+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            cellRow = consoleScanner.nextInt();
            while(cellRow<0 || cellRow>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+ROW_CELL+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                cellRow = consoleScanner.nextInt();
            }
                /*  # MOVEMENT CELL COL #
                    14|
                    15|Select a valid cell for the building phase
                    16|Cell col • >
                    17|
                 */
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.print(ANSI_GRAY+COL_CELL+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            cellCol = consoleScanner.nextInt();
            while(cellCol<0 || cellCol>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+COL_CELL+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                cellCol = consoleScanner.nextInt();
            }
            //Check if the inserted values belong to the worker view
            if(clientController.getWorkerViewCell(cellRow,cellCol))
                validMove=true;
            if(!validMove){
                    /*  # MOVEMENT INVALID CELL #
                        14|
                        15|Invalid cell... retry! • Select a valid cell for the building phase >
                        16|
                        17|
                    */
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_CELL+ANSI_WHITE+BUILDING_REQUEST+NEW_LINE);
            }

        }while(!validMove);
        printedLinesCounter+=1;
        clientController.playStepRequest(cellRow,cellCol);
    }

    @Override
    public void removeBlock(ClientController clientController) throws SantoriniException {
        //Local Variables
        boolean validMove=false;
        int cellRow=0,cellCol=0;
        currentPhase=phasesMap.get(4);
        renderBoard(decomposeWorkerView(clientController));
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+REMOVE_REQUEST+NEW_LINE);
        //Logic
        do{
                /*  # REMOVE TOWER CELL ROW #
                    14|
                    15|Select a valid tower for the remove phase
                    16|Tower cell row • >
                    17|
                 */
            System.out.print(ANSI_GRAY+TOWER_ROW_CELL+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            cellRow = consoleScanner.nextInt();
            while(cellRow<0 || cellRow>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+TOWER_ROW_CELL+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                cellRow = consoleScanner.nextInt();
            }
                /*  # REMOVE TOWER CELL COL #
                    14|
                    15|Select a valid tower for the remove phase >
                    16|Tower cell col • >
                    17|
                 */
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.print(ANSI_GRAY+TOWER_COL_CELL+ANSI_WHITE+CLI_INPUT);
            while(!consoleScanner.hasNextInt()){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                consoleScanner.next();
            }
            cellCol = consoleScanner.nextInt();
            while(cellCol<0 || cellCol>4){
                System.out.print(String.format(CURSOR_UP,1));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_COORDINATE+ANSI_GRAY+TOWER_COL_CELL+ANSI_WHITE+CLI_INPUT);
                while(!consoleScanner.hasNextInt()){
                    System.out.print(String.format(CURSOR_UP,1));
                    System.out.print(CLEAN);
                    System.out.print(ANSI_RED+NOT_A_NUMBER+ANSI_WHITE+CLI_INPUT);
                    consoleScanner.next();
                }
                cellCol = consoleScanner.nextInt();
            }
            //Check if the inserted values belong to the worker view
            if(clientController.getWorkerViewCell(cellRow,cellCol))
                validMove=true;
            if(!validMove){
                    /*  # REMOVE INVALID TOWER #
                        14|
                        15|Invalid Tower... retry! • Select a valid tower for the removal phase >
                        16|
                        17|
                    */
                System.out.print(String.format(CURSOR_UP,2));
                System.out.print(CLEAN);
                System.out.print(ANSI_RED+INVALID_TOWER_CELL+ANSI_WHITE+MOVEMENT_REQUEST+NEW_LINE);
            }

        }while(!validMove);
        printedLinesCounter+=1;
        clientController.playStepRequest(cellRow,cellCol);

    }

    /**
     * Asks to the player if he wants to skip an action
     * @return boolean
     */
    @Override
    public boolean askForSkip() {
        boolean answer;
        String userAnswer;
        currentPhase=phasesMap.get(2);
        renderBoard("Ask for skip"+BLANK);
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+SKIP_REQUEST+NEW_LINE);
        System.out.print(ANSI_GRAY+SKIP+ANSI_WHITE+CLI_INPUT);
        printedLinesCounter+=1;
        userAnswer=consoleScanner.next();
        while (!userAnswer.equalsIgnoreCase("yes") && !userAnswer.equalsIgnoreCase("no")){
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.println(ANSI_RED+INVALID_INPUT+ANSI_GRAY+SKIP+ANSI_WHITE+CLI_INPUT);
            userAnswer=consoleScanner.next();
        }
        answer= userAnswer.equalsIgnoreCase("yes");
        return answer;
    }

    /**
     * Asks to the player if he wants to repeat an action
     * @return boolean
     */
    @Override
    public boolean askForRepeat() {
        boolean answer;
        String userAnswer;
        currentPhase=phasesMap.get(2);
        renderBoard("Ask for repeat"+BLANK);
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+REPEAT_REQUEST+NEW_LINE);
        System.out.print(ANSI_GRAY+REPEAT+ANSI_WHITE+CLI_INPUT);
        printedLinesCounter+=1;
        userAnswer=consoleScanner.next();
        while (!userAnswer.equalsIgnoreCase("yes") && !userAnswer.equalsIgnoreCase("no")){
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.println(ANSI_RED+INVALID_INPUT+ANSI_GRAY+SKIP+ANSI_WHITE+CLI_INPUT);
            userAnswer=consoleScanner.next();
        }
        answer = userAnswer.equalsIgnoreCase("yes");
        return answer;
    }

    /**
     * Asks to the player which kind of turn he wants to play
     * @return boolean
     */
    @Override
    public boolean askForBasicTurn() {
        boolean answer;
        String userAnswer;
        currentPhase=phasesMap.get(5);
        renderBoard("Take a decision"+BLANK);
        System.out.print(String.format(CURSOR_UP,1));
        System.out.print(CLEAN);
        System.out.print(ANSI_WHITE+TYPE_REQUEST+NEW_LINE);
        System.out.print(ANSI_GRAY+TYPE+ANSI_WHITE+CLI_INPUT);
        userAnswer=consoleScanner.next();
        while (!userAnswer.equalsIgnoreCase("yes") && !userAnswer.equalsIgnoreCase("no")){
            System.out.print(String.format(CURSOR_UP,1));
            System.out.print(CLEAN);
            System.out.println(ANSI_RED+INVALID_INPUT+ANSI_GRAY+TYPE+ANSI_WHITE+CLI_INPUT);
            userAnswer=consoleScanner.next();
        }
        answer = userAnswer.equalsIgnoreCase("no");
        printedLinesCounter+=1;
        return answer;
    }

    /**
     * Prints a fatal error message and closes the client
     * @param exceptionName is Exception name
     */
    @Override
    public void callError(String exceptionName) {
        String userInput;
        System.out.println(ANSI_RED+String.format(FATAL_ERROR,exceptionName));
        System.out.print(ANSI_GRAY+EXIT+ANSI_WHITE+CLI_INPUT);
        userInput=consoleScanner.next();
        while (!userInput.equalsIgnoreCase("quit")){
            System.out.print(String.format(CURSOR_UP,1));
            System.out.println(CLEAN);
            System.out.print(ANSI_RED+INVALID_INPUT+ANSI_GRAY+EXIT+ANSI_WHITE+CLI_INPUT);
            userInput=consoleScanner.next();
        }
        System.out.println(GOODBYE+NEW_LINE+ANSI_GRAY+CLOSING);
        System.exit(0);
    }

    /**
     * Prints the match results
     * @param result is the result of the match
     */
    @Override
    public void callMatchResult(String result) {
        System.out.print(ANSI_WHITE+"RESULT:"+BLANK+result+NEW_LINE);
        System.out.println(ANSI_GRAY+CLOSING);
        System.exit(0);
    }

    public void setCurrentPhase(String currentPhase){this.currentPhase=currentPhase;}
    public void setOperationRepeated(){this.operationRepeated= !operationRepeated;}

}
