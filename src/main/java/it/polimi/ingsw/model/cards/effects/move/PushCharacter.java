package it.polimi.ingsw.model.cards.effects.move;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;


public class PushCharacter extends MoveEffect {
    /**
     * Class Constructor
     */
    public PushCharacter() {
        super();
        super.turnStructure = new ArrayList<>();
        super.turnStructure.add(Step.MOVE);
        super.turnStructure.add(Step.BUILD);
        super.turnStructure.add(Step.END);
    }



    /**
     * Allows you to choose a cell occupied by an enemy worker
     * @param selectedWorker is the worker selected by the current player
     */
    @Override
    public Cell[][] generateMovementMatrix(Worker selectedWorker) {
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        //filter cells with: workers of the same player of selectedWorker, higher than one than the worker, Domes
        Cell[][] minotaurMatrix = battlefield.getWorkerView(selectedWorker, (cell)->!cell.isFriendWorkerPresent(selectedWorker)
                && battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight() + 1 >= cell.getTower().getHeight()
                && !(cell.getTower().getLastBlock() == Block.DOME));

        //check valid movements in enemy's cells
        for(int i = 0; i < Battlefield.N_ROWS_VIEW; i++) {
            for (int j = 0; j < Battlefield.N_COLUMNS_VIEW; j++) {
                //check only possibles valid movements (by selected worker)
                if (minotaurMatrix[i][j] != null){
                    //check worker presence (enemy: due to getWorkerView)
                    if(minotaurMatrix[i][j].isWorkerPresent()){
                        //check possible player direction
                        int directionRow = i- selectedWorker.getRowWorker();
                        int directionCol = j - selectedWorker.getColWorker();
                        //check valid push direction inside battlefield
                        if(i+directionRow < Battlefield.N_ROWS_VIEW && j+directionCol < Battlefield.N_COLUMNS_VIEW) {
                            //get cell on that direction
                            Cell forcedMoveCell = battlefield.getCell(i + directionRow, j + directionCol);

                            //check another worker presence
                            if (forcedMoveCell.isWorkerPresent()) {
                                minotaurMatrix[i][j] = null;
                            }
                            //check dome presence
                            if (forcedMoveCell.getTower().getLastBlock() == Block.DOME) {
                                minotaurMatrix[i][j] = null;
                            }
                        }
                        else {
                            //can't push enemy outside battlefield
                            minotaurMatrix[i][j] = null;
                        }
                    }
                }
            }
        }

        return minotaurMatrix;
    }


    /**
     * Allows you to do a basic-move or to push away your enemy and move in his cell (only if you can push your enemy)
     * @param selectedWorker is the worker selected by the current player
     * @param newRow is the new x coordinate of the worker
     * @param newCol is the new y coordinate of the worker
     */
    @Override
    public void moveWorker(Worker selectedWorker, int newRow, int newCol) {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newRow][newCol] == null)
            throw new RuntimeException("Unexpected Error!");

        int directionRowSelectedWorker = newRow - selectedWorker.getRowWorker();
        int directionColSelectedWorker = newCol - selectedWorker.getColWorker();
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        Worker enemyWorker=null;

        //check enemy worker presence
        if(battlefield.getCell(newRow, newCol).isWorkerPresent())
            enemyWorker = battlefield.getCell(newRow,newCol).getWorker();
        //check towers levels
        int lvl_b = battlefield.getCell(selectedWorker.getRowWorker(), selectedWorker.getColWorker()).getTower().getHeight();
        int lvl_a = battlefield.getCell(newRow, newCol).getTower().getHeight();
        //enemy push
        if (enemyWorker != null) {
            enemyWorker.changeWorkerPosition(newRow + directionRowSelectedWorker, newCol + directionColSelectedWorker);
        }
        //basic move
        selectedWorker.changeWorkerPosition(newRow,newCol);
        //check win
        if(lvl_a - lvl_b == 1 && lvl_a == 3)
            reachedLevel3 = true;

        movesLeft--;
    }


}
