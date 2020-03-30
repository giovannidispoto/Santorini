package it.polimi.ingsw.model;

/**
 * Represents the blocks used to build the towers in the game
 * Each cell contains a tower, which starts from the GROUND level (basically it means that the tower has no levels)
 */
public enum Block {
    GROUND, //Grass
    LEVEL_1, //Basement
    LEVEL_2, //Apartment
    LEVEL_3, //Terrace
    DOME; //Dome

    //???
    private static Block[] vals = values();

    /**
     * Gets the next object of the tower structure
     * @return Block object
     */
    public Block next(){
        return vals[(this.ordinal()+1) % vals.length];
    }
}
