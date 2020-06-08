package it.polimi.ingsw.client.network.messagesInterfaces.basicInterfaces;

/**
 * Allows you to create a message to the server with the action & data field
 */
public class BasicMessageInterface {
    String action;
    Object data;

    /**
     * Create new message
     * @param action action
     * @param data data
     */
    public BasicMessageInterface(String action, Object data){
        this.action = action;
        this.data = data;
    }
}
