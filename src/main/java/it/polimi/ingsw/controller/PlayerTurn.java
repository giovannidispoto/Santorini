package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.effects.global.NoLevelUpCondition;
import it.polimi.ingsw.server.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * The PlayerTurn class encapsulate turn and split it in steps
 */
public class PlayerTurn {

    private List<Step> steps;
    private Match match;
    private Turn currentTurn;

    /**
     * Create PlayerTurn of a turn
     * @param turn turn
     * @param match match
     */
    public PlayerTurn(Turn turn, Match match){
        this.currentTurn = turn;
        steps = new ArrayList<>(turn.getTurnStructure());
        this.match = match;
        match.setSelectedWorker(match.getCurrentPlayer().getPlayerWorkers().get(0));

        //Check if first step is a move or a build, then update the correct matrix
        if(steps.get(0) == Step.MOVE || steps.get(0) == Step.MOVE_SPECIAL )
             updateMovmentMatrix();
        else if(steps.get(0) == Step.BUILD || steps.get(0) == Step.BUILD_SPECIAL)
            updateBuildingMatrix();
    }

    /**
     * Update movement matrix when a new currentWorker is selected
     * For global effect, add here..
     */
    public void updateMovmentMatrix(){
        match.getSelectedWorker().setWorkerView(currentTurn.generateMovementMatrix(match.getSelectedWorker()));
        match.getSelectedWorker().setWorkerView(NoLevelUpCondition.getInstance().applyEffect(match.getSelectedWorker()));
        match.getSelectedWorker().notifyUpdate();
    }

    public boolean isLoser(){
        List<Cell[][]> workerViews = new ArrayList<>();
        boolean looser = true;
        for(Worker w: match.getCurrentPlayer().getPlayerWorkers()){
            workerViews.add(currentTurn.generateMovementMatrix(w));
        }

        for(Cell[][] workerView: workerViews){
            for(int i = 0; i < Battlefield.N_ROWS_VIEW; i++){
                for(int j = 0; j < Battlefield.N_COLUMNS_VIEW; j++){
                    if(workerView[i][j] != null)
                        looser = false;
                }
            }
        }

        return looser;
    }

    /**
     * Update building matrix
     */
    public void updateBuildingMatrix(){
        match.getSelectedWorker().setWorkerView(currentTurn.generateBuildingMatrix(match.getSelectedWorker()));
        match.getSelectedWorker().notifyUpdate();
    }

    /**
     * Update remove matrix
     */
    private void updateRemoveMatrix(){
        match.getSelectedWorker().setWorkerView(currentTurn.generateRemoveMatrix(match.getSelectedWorker()));
        match.getSelectedWorker().notifyUpdate();
    }

    /**
     *  Perform a move
     * @param w worker
     * @param x row
     * @param y col
     */
    public boolean move(Worker w, int x, int y){
        //move
        currentTurn.moveWorker(w,x,y);
        currentTurn.checkLocalCondition(w);
        steps.remove(0);

        if(match.getWinner() != null)
            return true;
        else {
            updateBuildingMatrix();
            return false;
        }
    }

    /**
     * Skip current step.
     * Possible only if step is special move or special build
     */
    public void skip(){

        if(steps.get(0) == Step.MOVE_SPECIAL)
            currentTurn.skipMove();
        if(steps.get(0) == Step.BUILD_SPECIAL)
            currentTurn.skipBuild();

        steps.remove(0);

        //update matrix
        if(steps.get(0) == Step.BUILD)
            updateBuildingMatrix();
    }

    /**
     * Perform a build
     * @param w worker
     * @param x row
     * @param y col
     */
    public void build(Worker w, int x, int y){
        //build
        currentTurn.buildBlock(w,x,y);
        //generate remove matrix if is necessary
        steps.remove(0);

        //for turn that have a build before a move
        if(steps.get(0) == Step.MOVE)
            updateMovmentMatrix();

        //for turn that have remove after build
        if(steps.get(0) == Step.REMOVE)
            updateRemoveMatrix();
    }

    /**
     * Perform a remove
     * @param w Worker
     * @param x row
     * @param y col
     */
    public void remove(Worker w,int x, int y){
        //remove
        currentTurn.removeBlock(w,x,y);
        steps.remove(0);
    }

    /**
     * Pass Turn
     */
    public void passTurn(){
        //pass turn
        currentTurn.passTurn();
    }

    /**
     * Gets current step of the turn
     * @return current step of the turn
     */
    public Step getCurrentState(){
        return this.steps.get(0);
    }
    
}
