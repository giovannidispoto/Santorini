package it.polimi.ingsw.server.network.messagesInterfaces;

/**
 * CellMatrixResponse class represent format used for matrix exchange from client and server such as battlefield or worker view matrix
 */
public class CellMatrixResponse {
    private CellInterface[][] cellMatrix;

    /**
     * Create new Message Interface
     * @param cellMatrix data
     */
    public CellMatrixResponse(CellInterface[][] cellMatrix){
        this.cellMatrix = cellMatrix;
    }
}
