package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.network.actions.data.dataInterfaces.CellInterface;
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
        if(action.equals("setBattlefield")){
            //Awakens who was waiting Server Response
            synchronized (clientController.lockManager.lockSetBattlefield){
                clientController.lockManager.lockSetBattlefield.setUsed();
                clientController.lockManager.lockSetBattlefield.notify();
            }
        }
        else if (action.equals("getBattlefieldResponse")){
            //Awakens who was waiting Server Response
            synchronized (clientController.lockManager.lockGetBattlefield){
                clientController.lockManager.lockGetBattlefield.setUsed();
                clientController.lockManager.lockGetBattlefield.notify();
            }
        }
    }
}
