package it.polimi.ingsw.server.observers;

import it.polimi.ingsw.model.Player;


/**
 *  Interface used for observer Players chenage
 *  The update notify client about player that is removed from the game.
 */
public interface ObserverPlayers {
    /**
     * Update from Match
     * @param removedPlayer removed player from the game
     */
    public void update(Player removedPlayer);
}
