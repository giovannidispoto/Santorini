package it.polimi.ingsw.client.cli;
import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.clientModel.basic.Color;
import it.polimi.ingsw.client.clientModel.basic.DivinityCard;
import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.UIActions;
import it.polimi.ingsw.client.network.data.dataInterfaces.PlayerInterface;

import java.util.*;

/**
 * CLIBuilder contains everything you need to build the CLI and use it
 */
public class CLIBuilder implements UIActions{

    //------------------ # ANSI Colors 256 bit # ------------------
    private static final String CODE_BLUE = "33";
    private static final String CODE_LIGHTBLUE = "75";
    private static final String CODE_GRAY = "248";
    private static final String CODE_LIGHT_GRAY = "244";
    private static final String CODE_GREEN = "41";
    private static final String CODE_LIGHT_GREEN = "83";
    private static final String CODE_BROWN = "130";
    private static final String CODE_RED = "197";
    private static final String CODE_PURPLE = "99";
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
    private static final String BOARD_TITLE = "BOARD";
    private static final String PLAYERS_TITLE = "PLAYERS 👦🏼";
    private static final String TOWERS_TITLE = "FULL TOWERS 🏗";
    private static final String MOVES_TITLE = "PLAYER MOVES 🕹";
    private static final String PHASE_TITLE = "CURRENT PHASE 🚀";

    //------------------ # UI Data # ------------------
    private HashMap<Color,String> WorkerColorsMap;
    private CLIDataObject[] boardCellsContents; // Each element is a row ┃   ┃   ┃   ┃   ┃   ┃
    private List<String> playerMoves; //Available Moves for the Player
    private String currentPhase;
    private int fullTowersNumber;

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
    private static final String WAIT_TURN = "Wait for the end of the other players turns...";
    private static final String LOBBY_JOIN = "Joining the lobby...";

    //Cards
    private static final String CHOOSE_CARD = "Choose your card for this match 🕹";

    //Worker Placement
    private static final String ROW_WORKER = "Worker row • ";
    private static final String COL_WORKER = "Worker column • ";

    //------------------ # Successes Messages # ------------------

    //Web
    private static final String SUCCESSFUL_HANDSHAKING = "Connection established!";
    private static final String SUCCESSFUL_LOBBY_ACCESS = "You have correctly joined the lobby!";

    //Worker Placement
    private static final String SUCCESSFUL_PLACEMENT = "Worker Placed!";

    //------------------ # Failures and Errors Messages # ------------------

    //General Purpose
    private static final String NOT_A_NUMBER = "This is not a number...retry! • ";
    private static final String INVALID_INPUT = "Invalid input...retry! • ";

    //Web
    private static final String INVALID_IP = "Invalid IP...retry! • ";
    private static final String UNAVAILABLE_LOBBY = "The selected lobby is full or unavailable... try later 😭";
    private static final String NICKNAME_ERROR = "There is already a player with this nickname in the lobby...retry!";
    private static final String FAILED_CONNECTION = "Troubles with the connection...retry!";
    private static final String LOBBY_SIZE_ERROR = "This game is just for 2 or 3 people...retry! • ";
    private static final String PORT_SUGGESTION_ERROR = "This is not a number! We suggest you the port 1337 • ";

    //Cards
    private static final String INVALID_CARD = "Invalid card choice...retry! • ";

    //Fatal
    private static final String FATAL_ERROR = "Something broke down... what do you want to do? 🔥";
    private static final String GOODBYE = "Goodbye...hope to see you soon 😪";
    private static final String CLOSING = "Closing the program...";

    //Worker Placement
    private static final String OCCUPIED_POSITION = "Already occupied cell...retry! • ";
    private static final String INVALID_COORDINATE = "Invalid coordinate... [0 and 4] • ";

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
    private final int refreshableAreaHeight = 14;
    private final int editableRowCells = 5;

    //DONE: Class Constructor
    /**
     * Class Constructor
     */
    public CLIBuilder(String colorMode, ClientController clientController) {
        this.boardCellsContents = new CLIDataObject[5];
        this.playerMoves = new ArrayList<>();
        this.WorkerColorsMap = new HashMap<>();

        //DEBUG: Currently use Placement as the Current Phase etiquette
        this.currentPhase = "Placement";

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
        WorkerColorsMap.put(Color.GREY, ANSI_LIGHT_GRAY +WORKER);
    }

    //DONE: Implemented Support Methods

    /**
     * Extracts a single formatted String from the playerMoves list
     * @return String
     */
    public String transformMovesList(){
        StringBuilder availableMoves = new StringBuilder();
        for(String currentMove : playerMoves){
            availableMoves.append(currentMove).append(BLANK);
        }
        return availableMoves.toString();
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
    public void renderPlayersInfo(ClientController clientController){
        //Local Variables
        StringBuilder playerData = new StringBuilder();
        List<String> playersInfo = new ArrayList<>();
        //Request
        clientController.getPlayersRequest();
        //Logic
        System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        System.out.print(CLEAN);
        System.out.println(NEW_LINE+ANSI_WHITE+edge_distance+PLAYERS_TITLE+NEW_LINE);
        for(PlayerInterface currentPlayer : clientController.getPlayers()){
            playerData.append(edge_distance).append(String.format(playerDataTemplate, WorkerColorsMap.get(currentPlayer.getColor()), ANSI_WHITE+currentPlayer.getPlayerNickname(), currentPlayer.getCard()));
            playersInfo.add(playerData.toString());
            playerData.setLength(0);
        }
        for(String current : playersInfo)
            System.out.println(current);
        System.out.print(NEW_LINE+NEW_LINE);
        printedLinesCounter=0;
    }

    /**
     * Renders the CLI Data part
     * 0 |            BOARD
     * 1 |     0   1   2   3   4      FULL TOWERS 🏗
     * 2 |   ┏━━━┳━━━┳━━━┳━━━┳━━━┓    ┌╌╌╌┐
     * 3 | 0 ┃   ┃   ┃   ┃   ┃   ┃    ┊ 4 ┊
     * 4 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌┘
     * 5 | 1 ┃   ┃   ┃   ┃   ┃   ┃    CURRENT PHASE 🚀
     * 6 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌┐
     * 7 | 2 ┃   ┃   ┃   ┃   ┃   ┃    ┊ Building ┊
     * 8 |   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    └╌╌╌╌╌╌╌╌╌╌┘
     * 9 | 3 ┃   ┃   ┃   ┃   ┃   ┃    AVAILABLE MOVES 🎮
     * 10|   ┣━━━╋━━━╋━━━╋━━━╋━━━┫    ┌╌╌╌╌╌╌╌╌╌╌╌╌╌┐
     * 11| 4 ┃   ┃   ┃   ┃   ┃   ┃    ┊ [2|1] [0|2] ┊
     * 12|   ┗━━━┻━━━┻━━━┻━━━┻━━━┛    └╌╌╌╌╌╌╌╌╌╌╌╌╌┘
     * 13|
     * 14|
     * @param availableMoves is a formatted String with all the available moves for the player
     */
    public void renderBoard(String availableMoves){
        //Local Variables
        StringBuilder currentLine = new StringBuilder();
        int currentRow = 0;

        //Clean the screen
        System.out.print(String.format(CURSOR_UP,printedLinesCounter));
        System.out.print(CLEAN);

        //Print line 0
        currentLine.append(BLANK.repeat(boardTitleEdgeDistance));
        currentLine.append(ANSI_WHITE+BOARD_TITLE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 1
        currentLine.append(edge_distance);
        for(int i=0;i<5;i++)
            currentLine.append(String.format(horizontalNumberRowTemplate,i));
        currentLine.append(BLANK+edge_distance).append(ANSI_LIGHTBLUE+TOWERS_TITLE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 2
        currentLine.append(edge_distance+upperEdgeBoard+edge_distance+ANSI_LIGHTBLUE+upperEdgeTowers+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 3
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_LIGHTBLUE).append(String.format(intermediateEdgeTowers, fullTowersNumber)).append(ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 4
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance+ANSI_LIGHTBLUE+lowerEdgeTowers+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 5
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_RED+PHASE_TITLE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 6
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance);
        currentLine.append(ANSI_RED+L_THIN_T_CORNER);
        currentLine.append(H_THIN_LINE.repeat(Math.max(0, currentPhase.length() + 2)));
        currentLine.append(R_THIN_T_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 7
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_RED).append(String.format(intermediateEdgePhase, currentPhase)).append(ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 8
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance);
        currentLine.append(ANSI_RED+L_THIN_B_CORNER);
        currentLine.append(H_THIN_LINE.repeat(Math.max(0, currentPhase.length() + 2)));
        currentLine.append(R_THIN_B_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 9
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+MOVES_TITLE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);
        currentRow++;

        //Print line 10
        currentLine.append(edge_distance+intermediateEdgeBoard+edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+L_THIN_T_CORNER+H_THIN_LINE);
        currentLine.append(H_THIN_LINE.repeat(availableMoves.length()));
        currentLine.append(H_THIN_LINE+R_THIN_T_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 11
        currentLine.append(BLANK).append(currentRow).append(BLANK).append(V_LINE);
        for(int i=0;i<editableRowCells;i++)
            currentLine.append(boardCellsContents[currentRow].getCellContent(i)).append(V_LINE);
        currentLine.append(edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+V_THIN_LINE+BLANK);
        currentLine.append(availableMoves);
        currentLine.append(BLANK+V_THIN_LINE+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 12
        currentLine.append(edge_distance+lowerEdgeBoard+edge_distance);
        currentLine.append(ANSI_LIGHT_GREEN+L_THIN_B_CORNER+H_THIN_LINE);
        currentLine.append(H_THIN_LINE.repeat(availableMoves.length()));
        currentLine.append(H_THIN_LINE+R_THIN_B_CORNER+ANSI_WHITE);
        System.out.println(currentLine);
        currentLine.setLength(0);

        //Print line 13
        System.out.print(NEW_LINE);
    }

    //DONE: Implemented UI Methods
    /**
     * Allows the user to register himself to the server
     * @param clientController is the client-side controller
     */
    @Override
    public void setupConnection(ClientController clientController) {

    }

    /**
     * Allows the god player to pick up the cards for the match
     * @param clientController is the client-side controller
     */
    @Override
    public void pickCards(ClientController clientController) {

    }

    /**
     * Allows the user to choose his card for the match
     * @param clientController is the client-side controller
     */
    @Override
    public void chooseCard(ClientController clientController) {

    }

    /**
     * Allows the user to place his workers on the board
     * @param clientController is the client-side controller
     */
    @Override
    public void placeWorkers(ClientController clientController) {

    }

    //TODO: Unimplemented UI Methods
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
