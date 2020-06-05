package it.polimi.ingsw.server.network.messagesInterfaces;

import it.polimi.ingsw.model.Block;
import it.polimi.ingsw.model.Color;

/**
 * CellInterface class represent cell information sent to client
 */
public class CellInterface {
    private String player;
    private Color workerColor;
    private int height;
    private Block lastBlock;

    /**
     * Create new CellInterface
     * @param player player in the cell
     * @param workerColor worker color in the cell
     * @param height height of the tower
     * @param lastBlock last block on the cell
     */
    public CellInterface(String player, Color workerColor, int height, Block lastBlock){
        this.player = player;
        this.workerColor = workerColor;
        this.height = height;
        this.lastBlock = lastBlock;
    }
}
