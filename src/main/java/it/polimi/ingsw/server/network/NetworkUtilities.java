package it.polimi.ingsw.server.network;

/**
 * Class containing strings and utilities used in network messages
 */
public final class NetworkUtilities {
    //REGEX
    public static final String pongRegex = "^ *\\{ *\"action\" *: *\"pong\" *} *$";
    public static final String addPlayerRegex = "^ *\\{ *\"action\" *: *\"addPlayer\" *, *\"data\" *:.*";
    //ACTION FIELD
    public static final String SERVER_ERROR_ACTION = "serverError";
    public static final String PING_ACTION = "ping";
    public static final String BATTLEFIELD_UPDATE_ACTION = "battlefieldUpdate";
    public static final String WORKERVIEW_UPDATE_ACTION = "workerViewUpdate";
    //MESSAGES
    public static final String DISCONNECTION_ERROR = "One Client Disconnected - Game Interrupted";
}
