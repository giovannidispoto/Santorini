package it.polimi.ingsw.client.controller;

/**
 * Class containing the exception messages used by networks and controllers
 */
public final class ExceptionMessages {
    //Error Messages
    public static final String genericError = "Unexpected Error";
    public static final String IOSocketError = "I/O Socket Error";
    public static final String streamDownSocketError = "Server Went Offline or Closed Connection";
    public static final String pingSocketError = "Network Connection Absent - Game Interrupted";
    public static final String jsonError = "Json Error Parsing Net-Message";
    public static final String defaultServerError = "Server Error";
    //Game Messages
    public static final String loseMessage = "You have lost! 😡";
    public static final String winMessage = "You have won! 👑";
}
