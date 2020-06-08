package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces;

import it.polimi.ingsw.client.clientModel.basic.Block;
import it.polimi.ingsw.client.clientModel.basic.Color;

/**
 * CellInterface class represent cell information sent from server
 */
public class CellInterface {
    private String player;
    private Color workerColor;
    private final int height;
    private final Block lastBlock;

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

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public Color getWorkerColor() {
        return workerColor;
    }

    public void setWorkerColor(Color workerColor) {
        this.workerColor = workerColor;
    }

    public int getHeight() {
        return height;
    }

    public Block getLastBlock() {
        return lastBlock;
    }

}
