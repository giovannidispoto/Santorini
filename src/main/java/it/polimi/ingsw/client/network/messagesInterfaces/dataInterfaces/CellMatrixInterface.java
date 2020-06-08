package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces;

/**
 *  Allows you to manage CellInterface matrices
 */
public class CellMatrixInterface {
    private final CellInterface[][] cellMatrix;

    /**
     * Create new CellMatrix Interface
     * @param cellMatrix data
     */
    public CellMatrixInterface(CellInterface[][] cellMatrix){
        this.cellMatrix = cellMatrix;
    }

    /**
     * Return board of Cell data interface
     * @return cellMatrix
     */
    public CellInterface[][] getCellMatrix() {
        return cellMatrix;
    }
}
