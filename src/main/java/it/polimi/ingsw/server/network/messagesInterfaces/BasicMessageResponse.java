package it.polimi.ingsw.server.network.messagesInterfaces;

/**
 * BasicMessageResponse encapsulate all message to the client and represent messages standard format
 */
public class BasicMessageResponse {
    private String action;
    private Object data;

    /**
     * Create new BasicMessageResponse
     * @param action action response
     * @param data data to send
     */
    public BasicMessageResponse(String action, Object data){
        this.action = action;
        this.data = data;
    }
}
