package it.polimi.ingsw.model;

/**
 * Represent the block used in the game to build the towers
 * Ground represent default value that means "no tower"
 */
public enum Block {
    GROUND,
    LEVEL_1,
    LEVEL_2,
    LEVEL_3,
    DOME;

    private static Block[] vals = values();

    public Block next(){
        return vals[(this.ordinal()+1) % vals.length];
    }
}
