package it.polimi.ingsw.server.actions.data;

import java.util.List;

/**
 * WorkersIDInterface class represent response to getWorkers request
 */
public class WorkersIDResponse {
    private String action;
    private List<Integer> data;

    public WorkersIDResponse(String action, List<Integer> data){
        this.action = action;
        this.data = data;
    }
}
