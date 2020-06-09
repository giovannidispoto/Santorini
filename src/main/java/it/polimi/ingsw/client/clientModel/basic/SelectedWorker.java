package it.polimi.ingsw.client.clientModel.basic;

/**
 * Class that represents the position of the selected player
 */
public class SelectedWorker {
    private final int row;
    private final int column;

    /**
     * Position of the selected player on the battlefield
     * @param row battlefield
     * @param column battlefield
     */
    public SelectedWorker(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter for Row Selected Worker Position
     * @return row battlefield
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for Column Selected Worker Position
     * @return column battlefield
     */
    public int getColumn() {
        return column;
    }
}
