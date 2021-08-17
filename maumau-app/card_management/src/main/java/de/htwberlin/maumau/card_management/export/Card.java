package de.htwberlin.maumau.card_management.export;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Card.java<br>
 * The class describes a game card.
 * @author Süleyman Demir
 *
 */
@Embeddable
public class Card implements Serializable {
    private Color color;
    private CardValue cardValue;

    /**
     * Constructor of the class {@link Card}
     * @param color - Enum color of the card like HERZ, PIK..
     * @param cardValue - Enum cardValue of the card like ASS, 7, 8, 9, 10..
     */
    public Card(Color color, CardValue cardValue) {
        this.color = color;
        this.cardValue = cardValue;
    }



    /**
     * The method returns the card value and the color as a string.
     */
    @Override
    public String toString() {
            return this.color.name() + " " + this.cardValue.name();
    }

    /**
     * The method returns the {@link Color} of the card;
     * @return  {@link Color} of card
     */
    public Color getColor() {
        return color;
    }

    /**
     * The method sets the {@link Color} of the card
     * @param color -  {@link Color} of card
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * The method returns the {@link CardValue} of the card
     * @return - cardValue of card
     */
    public CardValue getCardValue() {
        return cardValue;
    }

    /**
     * The method sets the {@link CardValue} of the card
     * @param cardValue - cardValue of card
     */
    public void setCardValue(CardValue cardValue) {
        this.cardValue = cardValue;
    }


    /**
     * The method compares if the objects are an instance of card
     * @param o - Object o as card
     * @return - if Object are instance of card then true, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (o != null || o instanceof Card) {
            Card card = (Card) o;
            return this.getCardValue() == card.getCardValue() && this.getColor().equals(card.getColor());
        } else {
            return false;
        }
    }
}
