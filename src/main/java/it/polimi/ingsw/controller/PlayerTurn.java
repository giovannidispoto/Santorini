package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Turn;

import java.util.List;

/**
 *
 */
public class PlayerTurn {

    private List<Step> steps;
    private Turn currentTurn;


    public PlayerTurn(Turn turn){
        this.currentTurn = turn;
        steps = currentTurn.getTurnStructure();
    }

    public void move(int x, int y){
        //move
        steps.remove(0);
    }

    public void build(int x, int y){
        //build
        steps.remove(0);
    }

    public void remove(int x, int y){
        //remove
        steps.remove(0);
    }


    public void passTurn(){
        //pass turn
        currentTurn.passTurn();
    }

    public Step getCurrentState(){
        return this.steps.get(0);
    }
    
}
