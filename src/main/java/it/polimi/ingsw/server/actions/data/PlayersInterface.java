package it.polimi.ingsw.server.actions.data;

import java.util.List;

/**
 * PlayInterface class represent message response to getPlayer request
 */
public class PlayersInterface {
    private String action;
    private List<PlayerInterface> data;

    /**
     * Create new message
     * @param action action
     * @param data data
     */
    public PlayersInterface(String action, List<PlayerInterface> data){
        this.action = action;
        this.data = data;
    }
}
