package it.polimi.ingsw.client.clientModel;

import it.polimi.ingsw.client.network.data.dataInterfaces.CellInterface;

/**
 * Class used to represent battlefield in the client
 */
public class BattlefieldClient {
    public static final int N_ROWS = 5;
    public static final int N_COLUMNS = 5;
    public static final int MAX_TOWER_HEIGHT = 4;

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

    /**
     * Count FullTowers presents on the battlefield
     * @return int fullTowers number
     */
    public int countFullTowers(){
        int fullTowersCounter = 0;

        //check full towers presents on battlefieldBoard
        for(int x = 0; x < N_ROWS; x++) {
            for (int y = 0; y < N_COLUMNS; y++) {
                //count full towers
                if (battlefieldBoard[x][y].getHeight() == MAX_TOWER_HEIGHT){
                    fullTowersCounter++;
                }
            }
        }
        return fullTowersCounter;
    }
}
