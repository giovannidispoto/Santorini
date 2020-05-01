package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class SetPlayerCardCommand implements Command {
    private String playerNickname;
    private String card;
    private boolean result;

    /**
     *
     * @param playerNickname
     * @param card
     */
    public SetPlayerCardCommand(String playerNickname, String card){
        this.playerNickname = playerNickname;
        this.card = card;
        this.result = false;
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        if(controller.checkHandler(playerNickname, handler)) {
            controller.setPlayerCard(playerNickname, card);
            result = true;
        }else{
            result = false;
        }
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("setPlayerCardResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getResult(){
        return result;
    }
}
