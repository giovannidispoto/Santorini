package it.polimi.ingsw.server.actions.commands;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.server.actions.data.BasicErrorMessage;
import it.polimi.ingsw.server.actions.data.BasicMessageResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * GetCardsInGameCommand class represent getCardsInGame action from the client
 * used for requesting cards used in the game
 */
public class GetCardsInGameCommand implements Command{

    private List<DivinityCard> cards;

    public GetCardsInGameCommand(){
        this.cards = new ArrayList<>();
    }

    /**
     * Execute command on the server
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
       cards = controller.getCardsInGame();
       if(cards == null) {
           handler.responseQueue(new Gson().toJson(new BasicErrorMessage("invalidRequest")));
       }else {
           handler.responseQueue(new Gson().toJson(new BasicMessageResponse("getCardsInGameResponse", this)));

       }
        handler.sendMessageQueue();
    }
}
