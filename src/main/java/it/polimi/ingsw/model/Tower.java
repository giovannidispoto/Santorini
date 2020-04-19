package it.polimi.ingsw.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Tower class represents a tower object of the game
 */
public class Tower {
    List<Block> towerBlocks;

    /**
     * Class constructor
     * Towers are initialized to the GROUND level
     */
    public Tower(){
        towerBlocks = new LinkedList<>();
        towerBlocks.add(Block.GROUND);
    }

    /**
     * Checks tower status (completed or not)
     * @return boolean value : TRUE if completed - FALSE otherwise
     */
    public boolean isCompleted(){
        return getHeight() == 4;
    } //Maximum height is 4

    /**
     * Adds a specified block above the tower
     * @param block that has to be added
     */
    public void addBlock(Block block){
        towerBlocks.add(block);
    }

    /**
     * Create one more level on the tower
     */
    public void addNextBlock(){
        if(getLastBlock() == Block.DOME)
            throw new RuntimeException("Trying to add block on a dome");
        towerBlocks.add(getLastBlock().next());
        Battlefield.getBattlefieldInstance().notifyUpdate();
    }

    /**
     * Removes a specified block from the tower
     * @param block that has to be removed
     * @throws RuntimeException if the block passed is not the last one added to the tower or if the tower has no blocks
     */
    public void removeBlock(Block block) throws RuntimeException{
        if(towerBlocks.size() == 1)
            throw new RuntimeException("Trying to remove block in a empty tower");
        if(towerBlocks.get(towerBlocks.size()-1) != block)
            throw new RuntimeException("Trying to remove block that's not the latest of the tower");
         towerBlocks.remove(block);
    }

    /**
     * Remove the last block from the tower
     * @throws RuntimeException if the tower has no blocks
     */
    public void removeLatestBlock() throws RuntimeException{
        /* TODO: Check if tower is not completed => last block is a dome*/
        if(towerBlocks.get(towerBlocks.size()-1) == Block.DOME)
            throw new RuntimeException("Trying to remove Dome");
        if(towerBlocks.size() == 1 )
            throw new RuntimeException("Trying to remove block in a void tower");
        towerBlocks.remove(towerBlocks.size() - 1 );
    }

    /**
     * Provides height of the tower
     * @return integer value (the height of the tower)
     */
    public int getHeight(){
        return towerBlocks.size() - 1;
    }

    /**
     * Gets the last block added to the tower
     * @return Block object
     */
    public Block getLastBlock(){
        return towerBlocks.get(towerBlocks.size() - 1);
    }
}
