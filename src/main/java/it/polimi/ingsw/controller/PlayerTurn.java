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

    private final List<Step> steps;
    private final Match match;
    private final Turn currentTurn;

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
             updateMovementMatrix(steps.get(0));
        else if(steps.get(0) == Step.BUILD || steps.get(0) == Step.BUILD_SPECIAL)
            updateBuildingMatrix(steps.get(0));
    }

    /**
     * Update movement matrix when a new currentWorker is selected,
     * No (LOSE CHECK + NULL CHECK (skip))
     */
    public void sendMovementMatrix(){
        match.getSelectedWorker().setWorkerView(currentTurn.generateMovementMatrix(match.getSelectedWorker()));
        match.getSelectedWorker().setWorkerView(NoLevelUpCondition.getInstance().applyEffect(match.getSelectedWorker()));
        match.getSelectedWorker().notifyUpdate();
    }

    /**
     * Update movement matrix,
     * LOSE CHECK + NULL CHECK (skip)
     */
    public void updateMovementMatrix(Step currentStep){
        match.getSelectedWorker().setWorkerView(currentTurn.generateMovementMatrix(match.getSelectedWorker()));
        match.getSelectedWorker().setWorkerView(NoLevelUpCondition.getInstance().applyEffect(match.getSelectedWorker()));

        if(match.getSelectedWorker().isInvalidWorkerView()) {
            if(currentStep == Step.MOVE_SPECIAL){
                this.skip();
            }else {
                //TODO: Player LOSE
            }
        }
        else {
            match.getSelectedWorker().notifyUpdate();
        }
    }

    /**
     * Update building matrix,
     * LOSE CHECK + NULL CHECK (skip)
     */
    public void updateBuildingMatrix(Step currentStep){
        match.getSelectedWorker().setWorkerView(currentTurn.generateBuildingMatrix(match.getSelectedWorker()));

        if(match.getSelectedWorker().isInvalidWorkerView()) {
            if(currentStep == Step.BUILD_SPECIAL){
                this.skip();
            }else {
                //TODO: Player LOSE
            }
        }
        else {
            match.getSelectedWorker().notifyUpdate();
        }
    }

    /**
     * Update remove matrix,
     * NULL CHECK (skip)
     */
    private void updateRemoveMatrix(){
        //Change Selected Worker
        match.setSelectedWorker(currentTurn.changeWorkerPlayer(match.getSelectedWorker()));
        match.getSelectedWorker().setWorkerView(currentTurn.generateRemoveMatrix(match.getSelectedWorker()));

        if(match.getSelectedWorker().isInvalidWorkerView()) {
            this.skip();
        }
        else {
            match.getSelectedWorker().notifyUpdate();
        }
    }

    public boolean isLoser(){
        List<Cell[][]> workerViews = new ArrayList<>();

        for(Worker w: match.getCurrentPlayer().getPlayerWorkers()){
            workerViews.add(currentTurn.generateMovementMatrix(w));
        }

        for(Cell[][] workerView: workerViews){
            for(int i = 0; i < Battlefield.N_ROWS_VIEW; i++){
                for(int j = 0; j < Battlefield.N_COLUMNS_VIEW; j++){
                    if(workerView[i][j] != null)
                        return false;
                }
            }
        }

        return true;
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
        if(steps.get(0) == Step.MOVE || currentTurn.getMovesLeft() == 0)
            steps.remove(0);

        if(match.getWinner() != null)
            return true;
        //Build

        if(steps.get(0) == Step.BUILD){
            updateBuildingMatrix(steps.get(0));
        }

        if(steps.get(0) == Step.MOVE || steps.get(0) == Step.MOVE_SPECIAL) {
            updateMovementMatrix(steps.get(0));
        }

        if(steps.get(0) == Step.MOVE_UNTIL){
            //Check if the worker with the previous move has left the perimeter (skip action)
            if(Battlefield.getBattlefieldInstance().getPerimeterCells().contains(Battlefield.getBattlefieldInstance().getCell(x,y))){
                //Continue move until action
                updateMovementMatrix(steps.get(0));
            }
            else{
                //No longer in the perimeter
                this.skip();
            }
        }

        return false;
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
        if(steps.get(0) == Step.REMOVE)
            currentTurn.skipBuild();

        steps.remove(0);

        //update matrix
        if(steps.get(0) == Step.BUILD)
            updateBuildingMatrix(steps.get(0));
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

        //build special
        if(steps.get(0) == Step.BUILD_SPECIAL)
            updateBuildingMatrix(steps.get(0));

        //for turn that have a build before a move
        if(steps.get(0) == Step.MOVE)
            updateMovementMatrix(steps.get(0));

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
