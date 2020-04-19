package it.polimi.ingsw.model.network.action.data;

/**
 * Class used in update message of battlefield and worker view
 */
public class BattlefieldInterface {
    /* action is only workerViewUpdate or battlefieldUpdate */
    private String action;
    private CellInterface[][] battlefield;

    /**
     * Create new Message Interface
     * @param action action to notify
     * @param battlefield data
     */
    public BattlefieldInterface(String action,CellInterface[][] battlefield){
        this.battlefield = battlefield;
        this.action = action;
    }
}
