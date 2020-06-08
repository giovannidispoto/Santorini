package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase;

/**
 * Allows you to send the coordinates of the selected worker and the nickName of his Player
 */
public class SelectWorkerInterface {
    String playerNickname;
    int x;
    int y;

    /**
     * Create new data interface
     * @param playerNickname nickName
     * @param row battlefield row
     * @param col battlefield column
     */
    public SelectWorkerInterface(String playerNickname, int row, int col){
        this.playerNickname = playerNickname;
        this.x = row;
        this.y = col;
    }
}
