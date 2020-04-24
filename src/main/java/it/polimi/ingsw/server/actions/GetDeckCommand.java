package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;
import it.polimi.ingsw.server.actions.data.DeckResponse;

/**
 *
 */
public class GetDeckCommand implements Command{
    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
       Deck d =  controller.getDeck();
       handler.response(new Gson().toJson(new BasicMessageResponse("getDeck", new DeckResponse(d))));
    }
}
