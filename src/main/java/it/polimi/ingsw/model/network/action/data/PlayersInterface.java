package it.polimi.ingsw.model.network.action.data;

import java.util.List;

public class PlayersInterface {
    private String action;
    private List<PlayerInterface> data;

    public PlayersInterface(String action, List<PlayerInterface> data){
        this.action = action;
        this.data = data;
    }
}
