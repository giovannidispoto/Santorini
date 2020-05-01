package it.polimi.ingsw.client.clientModel;

import it.polimi.ingsw.client.network.actions.data.dataInterfaces.CellInterface;

/**
 * Class used to represent battlefield in the client
 */
public class BattlefieldClient {
    private CellInterface[][] battlefieldBoard;
    private static BattlefieldClient instance = null;

    /**
     * Factory method that returns the BattlefieldClient instance (Singleton)
     * @return BattlefieldClient object
     */
    public static BattlefieldClient getBattlefieldInstance(){
        if(instance == null)
            instance = new BattlefieldClient();
        return instance;
    }

    public void setBattlefieldBoard(CellInterface[][] battlefieldBoard) {
        this.battlefieldBoard = battlefieldBoard;
    }

    public CellInterface getCell(int x, int y){
        return battlefieldBoard[x][y];
    }

    /** Check if cell (x, y) is already occupied
     *
     * @param x battlefield row
     * @param y battlefield column
     * @return  true = occupied, false = NOT occupied
     */
    public boolean isCellOccupied(int x, int y){
        return (null != battlefieldBoard[x][y].getPlayer());
    }
}
