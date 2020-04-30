package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

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
        handler.responseQueue(new Gson().toJson(new BasicMessageInterface("setPickedCardsResponse", this)));
        handler.sendMessageQueue();
    }

    public boolean getResult(){
        return result;
    }
}
