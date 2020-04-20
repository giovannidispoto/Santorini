package it.polimi.ingsw.model.cards.effects.global;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Worker;

/**
 * The GlobalEffect class represent a card effect that works not only in the player's turn
 */
public abstract class GlobalEffect {

    /**
     *
     * @param worker Worker to apply the effect
     * @return worker view matrix that indicate available cell
     */
    public abstract Cell[][] applyEffect(Worker worker);
}
