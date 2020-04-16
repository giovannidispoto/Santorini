package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.NoLevelUpCondition;

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
    }

    /**
     *
     */
    public void updateBuildingMatrix(){
        match.getSelectedWorker().setWorkerView(currentTurn.generateBuildingMatrix(match.getSelectedWorker()));
    }

    /**
     *  Perform a move
     * @param w worker
     * @param x row
     * @param y col
     */
    public void move(Worker w, int x, int y){
        //move
        currentTurn.moveWorker(w,x,y);
        match.getSelectedWorker().setWorkerView(currentTurn.generateBuildingMatrix(match.getSelectedWorker()));
        steps.remove(0);
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
