package it.polimi.ingsw.client.network.actions.data.dataInterfaces;

import java.util.List;

public class PickedCardsInterface {
    List<String> cards;

    public PickedCardsInterface(List<String> cards) {
        this.cards = cards;
    }
}
