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

    /**
     * Gets card name
     * @return card name
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Gets card type
     * @return card type
     */
    public Type getCardType() {
        return cardType;
    }

    /**
     * Gets number of player allowed
     * @return number of player allowed
     */
    public int getNumberOfPlayersAllowed() {
        return numberOfPlayersAllowed;
    }

    /**
     * Gets card effect description
     * @return card effect description
     */
    public String getCardEffect() {
        return cardEffect;
    }
}
