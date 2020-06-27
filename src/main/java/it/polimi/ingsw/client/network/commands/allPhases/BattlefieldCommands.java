package it.polimi.ingsw.client.network.commands.allPhases;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.controller.GameState;
import it.polimi.ingsw.client.controller.WaitManager;
import it.polimi.ingsw.client.network.commands.Command;
import it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.CellInterface;
import it.polimi.ingsw.client.controller.ClientController;

/**
 * Class that manages the command: BattlefieldUpdate and GetBattlefieldResponse
 */
public class BattlefieldCommands implements Command {
    CellInterface[][] battlefield;
    String action;

    /**
     * Create command
     * @param battlefield battlefield
     * @param action action
     */
    public BattlefieldCommands(CellInterface[][] battlefield, String action) {
        this.battlefield = battlefield;
        this.action = action;
    }

    @Override
    public void execute(ClientController clientController) {
        BattlefieldClient.getBattlefieldInstance().setBattlefieldBoard(battlefield);
        if(action.equals("battlefieldUpdate")){
            //Only when the player is ready for/in the match these messages are valid
            if(clientController.getGameState() == GameState.MATCH){
                clientController.showToUserBattlefield();
            }
            //Asynchronous message
        }
        else if (action.equals("getBattlefieldResponse")){
            //Awakens who was waiting Server Response
            synchronized (WaitManager.waitGetBattlefield){
                WaitManager.waitGetBattlefield.setUsed();
                WaitManager.waitGetBattlefield.notify();
            }
        }
    }
}
