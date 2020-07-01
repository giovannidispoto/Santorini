package it.polimi.ingsw.model.cards.effects.build;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.controller.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * DomeEveryWhere Effect
 */
public class DomeEverywhere extends BuildEffect {

    /**
     * Class Constructor
     * @param turnStructure is the list of the steps for the turn
     */
    public DomeEverywhere(List<Step> turnStructure){
        super.turnStructure = new ArrayList<>(turnStructure);
    }


    /**
     * This method allows you to build a Dome everywhere (except where is a worker or a dome)
     * @param selectedWorker is the worker selected by the player at the beginning of the turn
     * @param newBlockRow is the x coordinate of the new block
     * @param newBlockCol is the y coordinate of the new block
     */
    @Override
    public void buildBlock(Worker selectedWorker, int newBlockRow, int newBlockCol) throws RuntimeException {
        //Check coordinates
        if(selectedWorker.getWorkerView()[newBlockRow][newBlockCol]==null)
            throw new RuntimeException("Unexpected Error!");
        //Add a Dome
        Battlefield battlefield = Battlefield.getBattlefieldInstance();
        battlefield.getCell(newBlockRow,newBlockCol).getTower().addBlock(Block.DOME);

        blocksLeft--;
    }
}
