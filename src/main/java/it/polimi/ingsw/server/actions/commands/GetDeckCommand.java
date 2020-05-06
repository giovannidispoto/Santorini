package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

/**
 *
 */
public class GetDeckCommand implements Command{
    private Deck deck;
    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
       this.deck =  controller.getDeck();
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("getDeckResponse", this)));
        handler.sendMessageQueue();
    }

}
