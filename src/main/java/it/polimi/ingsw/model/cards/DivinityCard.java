package it.polimi.ingsw.model.cards;

/**
 * The DivinityCard class represent the card used in the game
 */
public class DivinityCard {
    private final String cardName;
    private final Type cardType;
    private final int numberOfPlayersAllowed;
    private final String cardEffect;

    /**
     * Create divinity card
     * @param cardName Name of the card
     * @param cardType Type of the card, according to enum
     * @param numberOfPlayersAllowed Number of player
     */
    public DivinityCard(String cardName, Type cardType, String cardEffect, int numberOfPlayersAllowed) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.numberOfPlayersAllowed = numberOfPlayersAllowed;
        this.cardEffect = cardEffect;
    }

    public String getCardName() {
        return cardName;
    }

    public Type getCardType() {
        return cardType;
    }

    public int getNumberOfPlayersAllowed() {
        return numberOfPlayersAllowed;
    }

    public String getCardEffect() {
        return cardEffect;
    }
}
