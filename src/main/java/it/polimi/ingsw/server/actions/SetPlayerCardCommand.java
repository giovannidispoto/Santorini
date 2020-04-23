package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class SetPlayerCardCommand implements Command {
    private String playerNickname;
    private String card;

    /**
     *
     * @param playerNickname
     * @param card
     */
    public SetPlayerCardCommand(String playerNickname, String card){
        this.playerNickname = playerNickname;
        this.card = card;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.setPlayerCard(playerNickname,card);
    }
}
