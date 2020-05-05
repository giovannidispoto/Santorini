package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

import java.util.List;

public class SetPickedCardsCommand implements Command {
    private List<String> cards;
    private boolean result;

    public SetPickedCardsCommand(List<String> cards){
        this.cards = cards;
    }

    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.setPickedCards(cards);
        result = true;
        handler.responseQueue(new Gson().toJson(new BasicMessageResponse("setPickedCardsResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getResult(){
        return result;
    }
}
