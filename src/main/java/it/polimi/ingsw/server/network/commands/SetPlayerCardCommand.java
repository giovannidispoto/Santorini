package it.polimi.ingsw.server.network.commands;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.network.ClientHandler;

/**
 * SetPlayerCard class represent setPlayerCard action used for
 * adding a card to player
 */
public class SetPlayerCardCommand implements Command {
    private String playerNickname;
    private String card;
    private boolean result;

    /**
     *
     * @param playerNickname player nickname
     * @param card card choosed
     */
    public SetPlayerCardCommand(String playerNickname, String card){
        this.playerNickname = playerNickname;
        this.card = card;
        this.result = false;
    }

    /**
     * Execute command on the server
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        if(controller.checkHandler(playerNickname, handler)) {
            controller.setPlayerCard(playerNickname, card);
           // handler.responseQueue(new Gson().toJson(new BasicMessageResponse("setPlayerCardResponse", this)));
            handler.sendMessageQueue();
        }
    }

}
