package it.polimi.ingsw.server.actions.data;

import java.util.List;

/**
 * PlayInterface class represent message response to getPlayer request
 */
public class PlayersResponse {
    private String action;
    private List<PlayerInterface> data;

    /**
     * Create new message
     * @param action action
     * @param data data
     */
    public PlayersResponse(String action, List<PlayerInterface> data){
        this.action = action;
        this.data = data;
    }
}
