package it.polimi.ingsw.server.actions.data;

public class BasicMessageResponse {
    private String action;
    private Object data;

    public BasicMessageResponse(String action, Object data){
        this.action = action;
        this.data = data;
    }
}
