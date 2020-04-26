package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.server.ClientHandler;

/**
 *
 */
public class GetDeckCommand implements Command{
    private Deck deck;
    private boolean result;
    /**
     * Execute command
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
       this.deck =  controller.getDeck();
       this.result = true;
        handler.response(new Gson().toJson(new BasicMessageInterface("getDeckResponse", this)));
    }

    /**
     * Gets status
     * @return status
     */
    public boolean getResult(){
        return result;
    }

    /**
     * Gets deck
     * @return deck
     */
    public Deck getDeck(){
        return deck;
    }
}
