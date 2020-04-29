package it.polimi.ingsw.server.actions;

import com.google.gson.Gson;
import it.polimi.ingsw.client.network.actions.data.basicInterfaces.BasicMessageInterface;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.cards.DivinityCard;
import it.polimi.ingsw.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class GetCardsInGameCommand implements Command{

    private List<String> cards;

    public GetCardsInGameCommand(){
        this.cards = new ArrayList<>();
    }

    /**
     *
     * @param controller context
     * @param handler context
     */
    @Override
    public void execute(Controller controller, ClientHandler handler) {
       cards = controller.getCardsInGame();
       handler.responseQueue(new Gson().toJson(new BasicMessageInterface("getCardsInGameResponse", this)));
       handler.sendMessageQueue();
    }
}
