package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Step;
import it.polimi.ingsw.model.Turn;

/**
 *
 */
public class PlayerTurn {

    private Step currentState;
    private Turn currentTurn;


    public PlayerTurn(Turn turn){
        this.currentTurn = turn;
        currentState = turn.getTurnStructure().get(0);
    }

    public void move(){
        //move
    }

    public void build(){
        //build
    }

    public void passTurn(){
        //pass turn
    }

    public Step getCurrentState(){
        return this.currentState;
    }
    
}
