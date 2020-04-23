package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

/**
 * Class used in update command of battlefield and worker view
 * Needed for Deserialization
 */
public class BattlefieldInterface {
    private CellInterface[][] battlefield;

    /**
     * Create new Message Interface
     * @param battlefield data
     */
    public BattlefieldInterface(CellInterface[][] battlefield){
        this.battlefield = battlefield;
    }

    //return board of data interface
    public CellInterface[][] getBattlefield() {
        return battlefield;
    }
}
