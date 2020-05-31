package it.polimi.ingsw.client.clientModel.basic;

public class SelectedWorker {
    private final int row;
    private final int column;

    public SelectedWorker(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
