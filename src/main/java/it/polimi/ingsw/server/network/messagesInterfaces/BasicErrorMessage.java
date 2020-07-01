package it.polimi.ingsw.server.network.messagesInterfaces;

/**
 * Error Message Interfce
 * */
public class BasicErrorMessage {
    private String error;

    /**
     * Create new error message
     * @param error error
     */
    public BasicErrorMessage(String error){
        this.error = error;
    }
}
