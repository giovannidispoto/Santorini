package it.polimi.ingsw.client.network.actions;

import it.polimi.ingsw.client.clientModel.BattlefieldClient;
import it.polimi.ingsw.client.network.actions.data.CellInterface;
import it.polimi.ingsw.client.controller.Controller;


public class BattlefieldUpdateCommand implements Command {
    CellInterface[][] battlefield;

    /**
     * Create command
     * @param battlefield battlefield
     */
    public BattlefieldUpdateCommand(CellInterface[][] battlefield) {
        this.battlefield = battlefield;
    }

    @Override
    public void execute(Controller controller) {
        BattlefieldClient.getBattlefieldInstance().setBattlefieldBoard(battlefield);
    }
}
