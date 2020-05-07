package it.polimi.ingsw.client.network.messagesInterfaces.dataInterfaces.lobbyPhase;

import java.util.List;

public class PickedCardsInterface {
    List<String> cards;

    public PickedCardsInterface(List<String> cards) {
        this.cards = cards;
    }
}
