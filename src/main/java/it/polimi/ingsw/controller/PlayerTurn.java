package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

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
        updateMovmentMatrix();
    }

    /**
     * Update movement matrix when a new currentWorker is selected
     */
    public void updateMovmentMatrix(){
        match.getSelectedWorker().setWorkerView(currentTurn.generateMovementMatrix(match.getSelectedWorker()));
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
