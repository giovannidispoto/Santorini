package it.polimi.ingsw.server.consoleUtilities;

import java.util.UUID;

public class PrinterClass {
    private static final PrinterClass instance = new PrinterClass();

    public static final String ansiRED      = "\u001B[31m";
    public static final String ansiMAGENTA  = "\u001B[95m";
    public static final String ansiYELLOW  = "\u001B[93m";
    public static final String ansiGREEN    = "\u001B[32m";
    public static final String ansiBLUE     = "\u001B[34m";
    public static final String ansiRESET    = "\u001B[0m";
    public static final String nextLine     = System.lineSeparator();
    public static final String consoleSeparator = "----------------------------------------------------------------------------------------------------------------";

    public static final String CLEAN = "\u001b[0J";
    public static final String INITIALIZE_SCREEN = "\033[0;0H";

    public static final String jsonParseError = ansiRED + "FAILED-JsonParse" + ansiRESET;
    public static final String timerTimeoutError = ansiBLUE + "Ping_Schedule_Already-Cancelled" + ansiRESET;

    public static final String socketClosedMessage = ansiBLUE+"Socket Closed"+ansiRESET;
    public static final String serverReadyMessage = ansiGREEN + "Server READY" + ansiRESET + nextLine + consoleSeparator;

    /**
     * Variable used in case of debugging, must be set to false in case of publication of the program
     */
    public boolean printDebugInfo = false;


    /**
     * Get the class used for console printing
     * @return PrinterClass object
     */
    public static PrinterClass getPrinterInstance() {
        return instance;
    }

    /**
     * Enable or disable debug messages on cli
     * @param printDebugInfo true = active
     */
    public void setPrintDebugInfo(boolean printDebugInfo) {
        this.printDebugInfo = printDebugInfo;
    }

    //---------------------     Console Printers

    public void printPingTimeout(String nickName, boolean lobbyStarted, boolean isMustStopExecution){
        System.out.println(ansiRED + "Ping_Timeout_" + nickName + ansiRESET + " -isLobbyStart:" + lobbyStarted + " -isStoppedByServer:" + isMustStopExecution);
    }

    public void printClientDisconnected(String nickName){
        System.out.println(ansiRED + "Client-Disconnected-Suddenly_NickName: " + nickName + ansiRESET);
    }

    public void printPlayerWaitingRemoved(String nickName){
        System.out.println(ansiRED + "Player-Waiting-LobbyStart-Removed: " + nickName + ansiRESET);
    }

    public void printClientDeleted(String nickName){
        System.out.println(ansiRED + "Client-Deleted_NickName: " + nickName + ansiRESET);
    }

    public void printPlayerEliminated(String nickName, UUID lobbyID){
        System.out.println(ansiBLUE + "Player: " + nickName + " Eliminated from the game: " + lobbyID + ansiRESET);
    }

    public void printGameEnd(String nickName){
        System.out.println(ansiRED + "GameEnd_NickName: " + nickName + ansiRESET);
    }

    public void printGameTampering(String nickName){
        System.out.println(ansiMAGENTA + "!!!-> Player: " + nickName + " Disconnected From The Game by Server, Cause: Game Tampering <-!!!" + ansiRESET);
    }

    public void printWinner(String nickName){
        System.out.println(ansiGREEN + "Winner: " + nickName + ansiRESET);
    }

    /**
     * Print the socket message only if debugging is active on the server (debug set in PrinterClass)
     * @param line received from the socket
     * @param nickName player NickName registered in the server
     */
    public void printSocketMessage(String line, String nickName){
        printDebugMessage("Received: " + line + " From:" + nickName);
    }

    /**
     * Print the passed message only if debugging is active on the server (debug set in PrinterClass)
     * @param message to print
     */
    public void printDebugMessage(String message){
        if(printDebugInfo)
            System.out.println(ansiYELLOW + message + ansiRESET);
    }

    /**
     * Print the passed message immediately
     * @param message to print
     */
    public void printMessage(String message){
        System.out.println(message);
    }

}
