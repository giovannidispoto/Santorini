package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.matchPhase;

/**
 * Allows you to send the coordinates of a action on the battlefield to the server
 */
public class PlayStepInterface {
    int x;
    int y;

    /**
     * Create new data interface
     * @param row battlefield row
     * @param col battlefield column
     */
    public PlayStepInterface(int row, int col){
        this.x = row;
        this.y = col;
    }
}
