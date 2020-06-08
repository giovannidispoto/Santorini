package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

import java.util.List;

/**
 * Allows the cards chosen by the god player to be sent to the server
 */
public class PickedCardsInterface {
    List<String> cards;

    /**
     * Create new data interface
     * @param cards List containing the chosen cards
     */
    public PickedCardsInterface(List<String> cards) {
        this.cards = cards;
    }
}
