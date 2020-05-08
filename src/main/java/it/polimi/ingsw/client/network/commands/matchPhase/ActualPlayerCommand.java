package it.polimi.ingsw.client.network.commands.matchPhase;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;


public class ActualPlayerCommand implements Command {

    String playerNickname;

    public ActualPlayerCommand(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    @Override
    public void execute(ClientController clientController) {
        clientController.setActualPlayer(this.playerNickname);
        //Awakens who was waiting Server Response
        synchronized (WaitManager.waitActualPlayer){
            WaitManager.waitActualPlayer.setUsed();
            WaitManager.waitActualPlayer.notify();
        }
    }
}
