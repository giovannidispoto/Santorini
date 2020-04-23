package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

public class CellMatrixInterface {
    private CellInterface[][] cellMatrix;

    /**
     * Create new Message Interface
     * @param cellMatrix data
     */
    public CellMatrixInterface(CellInterface[][] cellMatrix){
        this.cellMatrix = cellMatrix;
    }

    //return board of data interface
    public CellInterface[][] getCellMatrix() {
        return cellMatrix;
    }
}
