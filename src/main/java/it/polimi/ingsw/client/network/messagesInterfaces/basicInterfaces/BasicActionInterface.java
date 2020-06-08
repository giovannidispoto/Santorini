package it.polimi.ingsw.client.network.messagesInterfaces.basicInterfaces;

/**
 * Allows you to create a message to the server with only the action field
 */
public class BasicActionInterface {
    String action;

    /**
     * Create new message
     * @param action action
     */
    public BasicActionInterface(String action){
        this.action = action;
    }
}
