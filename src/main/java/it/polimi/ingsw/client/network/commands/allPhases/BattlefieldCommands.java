package it.polimi.ingsw.client.network.commands.allPhases;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.network.commands.Command;
import it.polimi.ingsw.client.network.data.dataInterfaces.CellInterface;
import it.polimi.ingsw.client.controller.ClientController;


public class BattlefieldCommands implements Command {
    CellInterface[][] battlefield;
    String action;

    /**
     * Create command
     * @param battlefield battlefield
     */
    public BattlefieldCommands(CellInterface[][] battlefield, String action) {
        this.battlefield = battlefield;
        this.action = action;
    }

    @Override
    public void execute(ClientController clientController) {
        BattlefieldClient.getBattlefieldInstance().setBattlefieldBoard(battlefield);
        if(action.equals("battlefieldUpdate")){
            //clientController.showToUserBattlefield();
            //Awakens who was waiting Server Response
            synchronized (clientController.waitManager.waitBattlefieldUpdate){
                clientController.waitManager.waitBattlefieldUpdate.setUsed();
                clientController.waitManager.waitBattlefieldUpdate.notify();
            }
        }
        else if (action.equals("getBattlefieldResponse")){
            //Awakens who was waiting Server Response
            synchronized (clientController.waitManager.waitGetBattlefield){
                clientController.waitManager.waitGetBattlefield.setUsed();
                clientController.waitManager.waitGetBattlefield.notify();
            }
        }
    }
}
