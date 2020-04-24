package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.server.ClientHandler;

import java.util.List;

public class SetPickedCardsCommand implements Command {
    private List<String> cards;

    public SetPickedCardsCommand(List<String> cards){
        this.cards = cards;
    }

    @Override
    public void execute(Controller controller, ClientHandler handler) {
        controller.setPickedCards(cards);
    }
}
