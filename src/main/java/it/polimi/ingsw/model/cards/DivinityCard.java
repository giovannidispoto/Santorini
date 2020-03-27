package it.polimi.ingsw.model.cards;

/**
 * The DivinityCard class represent the card used in the game
 */
public class DivinityCard {
    private final String cardName;
    private final String cardImage;
    private final Type cardType;
    private final int numberOfPlayersAllowed;
    private final String cardEffect;

    /**
     * Create divinity card
     * @param cardName Name of the card
     * @param cardImage Image of the card
     * @param cardType Type of the card, according to enum
     * @param numberOfPlayersAllowed Number of player
     */
    public DivinityCard(String cardName, String cardImage, Type cardType, String cardEffect, int numberOfPlayersAllowed) {
        this.cardName = cardName;
        this.cardImage = cardImage;
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
}
