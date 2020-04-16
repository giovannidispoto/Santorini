package it.polimi.ingsw.model.network.action;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;

public class AddPlayerCommand implements Command {

    PlayerInterface player;

    public AddPlayerCommand(PlayerInterface player) {
        this.player = player;
    }

    public void execute(Controller c) {
        try {
            c.addNewPlayer(player.getPlayerNickname(), player.getDate(), player.getColor(), player.getCard());
            System.out.println("Added new player: " + player.getPlayerNickname());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
