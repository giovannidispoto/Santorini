package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/** State of the game used for checking if requests are done right from the client*/
public class GameState {
    private List<GameStep> steps;

    public GameState(){
        this.steps = new ArrayList<>();
        this.steps.add(GameStep.CREATE_LOBBY);
        this.steps.add(GameStep.PICKING_CARDS);
        this.steps.add(GameStep.SETTING_CARDS);
        this.steps.add(GameStep.ADDING_WORKER);
        this.steps.add(GameStep.START_TURN);
    }

    /**
     * Gets state of the game
     * @return state
     */
    public GameStep getState(){
        return steps.get(0);
    }

    /**
     * Go to next state
     */
    public void nextState(){
        this.steps.remove(0);
    }
}
