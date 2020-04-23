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
    private static final String WAITING_ALERT = ANSI_WHITE+"Wait for your turn..."+ANSI_RST;

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

    //Players Box
    private static final String DOUBLE_L_T_CORNER = "╔";
    private static final String DOUBLE_R_T_CORNER = "╗";
    private static final String DOUBLE_L_B_CORNER = "╚";
    private static final String DOUBLE_R_B_CORNER = "╝";
    private static final String DOUBLE_H_LINE = "═";
    private static final String DOUBLE_V_LINE = "║";

    //Data Boxes
    protected static final String DOT_H_LINE = "╍";
    protected static final String DOT_V_LINE = "╏";

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
    private static final String SETPLAYERS = "You're the first player in the lobby! Set the number of players for this match 👦🏼";
    private HashMap<Integer,Color> COLORSMAP;

    //Players Informations Box
    protected static final String WORKER = "ᳵ";
    private HashMap<Color,String> playerColors;

    /**
     * Class Constructor
     */
    public CLIBuilder() {
        this.boardCellsContents = new CLIDataObject[5];
        this.playerMoves = new ArrayList<>();
        this.COLORSMAP = new HashMap<>();
        this.playerColors = new HashMap<>();
        this.currentPhase = null;
        this.numberFullTowers = 0;
        this.rowCounter = 0;
        COLORSMAP.put(0,Color.BLUE);
        COLORSMAP.put(1,Color.GREY);
        COLORSMAP.put(2,Color.BROWN);
        playerColors.put(Color.BLUE,ANSI_BLUE+WORKER);
        playerColors.put(Color.BROWN,ANSI_BROWN+WORKER);
        playerColors.put(Color.GREY,ANSI_GRAY+WORKER);
    }

    public void printMessageBox(String message){
        int messageLength = message.length();
        System.out.print(ANSI_PURPLE+L_THIN_T_CORNER);
        //+2 to consider blank spaces between the message and the lateral edges
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
     * Renders the not updatable part of the CLI (Players Informations Box)
     */
    public void staticCliRender(ClientController clientController){
        int maxLength=0;
        String playerWorkerColor;
        String playerNickname;
        String playerCard;
        List<String> playersInformations = new ArrayList<>();
        StringBuilder playerInfo = new StringBuilder();
        System.out.print(ANSI_WHITE+PLAYERS_TITLE+NEW_LINE);
        clientController.getPlayersRequest();
        for(PlayerInterface current : clientController.getPlayers()){
            playerWorkerColor=playerColors.get(current.getColor());
            playerInfo.append(DOUBLE_V_LINE + BLANK).append(playerWorkerColor);
            playerNickname = current.getPlayerNickname();
            playerInfo.append(BLANK).append(ANSI_WHITE).append(playerNickname);
            playerCard = current.getCard();
            playerInfo.append(V_THIN_LINE).append(playerCard).append(BLANK+DOUBLE_V_LINE);
            playersInformations.add(playerInfo.toString());
        }
        for(String current : playersInformations){
            if(current.length()>maxLength)
                maxLength=current.length();
        }
        System.out.print(ANSI_WHITE+DOUBLE_L_T_CORNER);
        for(int i=0;i<maxLength;i++)
            System.out.print(DOUBLE_H_LINE);
        System.out.print(DOUBLE_R_T_CORNER+NEW_LINE);
        for(String current : playersInformations){
            System.out.print(current+NEW_LINE);
        }
        System.out.print(DOUBLE_L_B_CORNER);
        for(int i=0;i<maxLength;i++)
            System.out.print(DOUBLE_H_LINE);
        System.out.print(DOUBLE_R_B_CORNER+NEW_LINE);
    }
    /**
     * Renders the updatable part of the CLI
     */
    public void dynamicCliRender(){

    }

    //UI Actions Methods
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
    public void selectCard(ClientController clientController) {

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

    }


}
