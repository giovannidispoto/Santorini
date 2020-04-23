package it.polimi.ingsw.server.actions.data;

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
