package it.polimi.ingsw.model;

import java.util.LinkedList;
import java.util.List;

/**
 * The Tower class represent building inside the game
 */
public class Tower {
    List<Block> towerBlocks;

    /**
     * Costruct the tower and initialize it at ground level
     */
    public Tower(){
        towerBlocks = new LinkedList<>();
        towerBlocks.add(Block.GROUND);
    }

    /**
     * Provide height of the tower
     * @return height of the tower
     */
    public int getHeight(){
        return towerBlocks.size() - 1;
    }

    /**
     * Check if the tower is completed or not
     * @return true if the tower is completed, false otherwise
     */
    public boolean isCompleted(){
        return getHeight() == 4;
    }

    /**
     *
     * @return
     */
    public Block getLastBlock(){
        return towerBlocks.get(towerBlocks.size() - 1);
    }

    /**
     * Add the block into the tower
     * @param block block to add
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
    }

    /**
     * Remove the block from the tower
     * @param block block to delete
     * @throws RuntimeException if the block passed is not the latest in the tower or if the tower is empty
     */
    public void removeBlock(Block block) throws RuntimeException{
        if(towerBlocks.size() == 1)
            throw new RuntimeException("Trying to remove block in a empty tower");
        if(towerBlocks.get(towerBlocks.size()-1) != block)
            throw new RuntimeException("Trying to remove block that's not the latest of the tower");
         towerBlocks.remove(block);
    }

    /**
     * Remove the latest block from the tower
     * @throws RuntimeException if the tower is empty
     */
    public void removeLatestBlock() throws RuntimeException{
        /* TODO: Check if tower is not completed => last block is a dome*/
        if(towerBlocks.get(towerBlocks.size()-1) == Block.DOME)
            throw new RuntimeException("Trying to remove Dome");
        if(towerBlocks.size() == 1 )
            throw new RuntimeException("Trying to remove block in a void tower");
        towerBlocks.remove(towerBlocks.size() - 1 );
    }

}
