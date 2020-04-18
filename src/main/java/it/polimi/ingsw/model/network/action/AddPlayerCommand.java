package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.network.action.data.PlayerInterface;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;

public class AddPlayerCommand implements Command {

    PlayerInterface player;

    public AddPlayerCommand(PlayerInterface player) {
        this.player = player;
    }

    public void execute(Controller controller, ClientHandler handler) {
        try {
            controller.addNewPlayer(player.getPlayerNickname(), player.getDate(), player.getColor(), player.getCard());
            controller.addWorkers(player.getPlayerNickname(), handler);
            controller.registerHandler(player.getPlayerNickname(), handler);
            System.out.println("Added new player: " + player.getPlayerNickname());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
