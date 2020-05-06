package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

import java.util.List;
/**
 * SetPickedCards class represent setPickedCards action used for
 * passing cards choosed by the god player to game
 */
public class SetPickedCardsCommand implements Command {
    private List<String> cards;

    /**
     * Create Command
     * @param cards cards choosed
     */
    public SetPickedCardsCommand(List<String> cards){
        this.cards = cards;
    }

    /**
     * Execute on the server
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.setPickedCards(cards);
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("setPickedCardsResponse", this)));
        handler.sendMessageQueue();
    }

}
