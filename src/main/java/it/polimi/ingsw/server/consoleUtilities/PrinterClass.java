package it.polimi.ingsw.server.consoleUtilities;

public final class PrinterClass {
    public static final String ansiRED      = "\u001B[31m";
    public static final String ansiMAGENTA  = "\u001B[95m";
    public static final String ansiGREEN    = "\u001B[32m";
    public static final String ansiBLUE     = "\u001B[34m";
    public static final String ansiRESET    = "\u001B[0m";
    public static final String nextLine     = System.lineSeparator();
    public static final String consoleSeparator = "----------------------------------------------------------------------------------------------------------------";

    public static final String CLEAN = "\u001b[0J";
    public static final String INITIALIZE_SCREEN = "\033[0;0H";

    //Variable used in case of debugging, must be set to false in case of publication of the program
    public static boolean printDebugInfo = false;
}
