package it.polimi.ingsw.client.clientModel.basic;

/**
 * The DivinityCard class represent the card used in the game
 */
public class DivinityCard {

    private final String cardName;
    private final Type cardType;
    private final boolean chooseBasic;
    private final int numberOfPlayersAllowed;
    private final String cardEffect;

    /**
     * Create divinity card
     * @param cardName Name of the card
     * @param cardType Type of the card, according to enum
     * @param numberOfPlayersAllowed Number of player
     * @param chooseBasic request to user
     * @param cardEffect effect of the card
     */
    public DivinityCard(String cardName, Type cardType, boolean chooseBasic, String cardEffect, int numberOfPlayersAllowed) {
        this.cardName = cardName;
        this.cardType = cardType;
        this.chooseBasic = chooseBasic;
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

    /**
     * Return true if the god needs a choice before turn about type of the turn
     * @return true if god need a choose, false otherwise
     * */
    public boolean isChooseBasic() {
        return chooseBasic;
    }
}
