package de.htwberlin.maumau.card_management.export;

import java.util.List;

/**
 * CardService.java<br>
 * The interface is a service for the game card.
 *  @author Süleyman Demir
 */
public interface CardService {

    /**
     * The methods creates the game cards.
     * @return a list of all created cards.
     */
    List<Card> createCards();

    /**
     * The methods returns all combinations of the color for the card.
     * For example, if you choose 7 as card value, then it returns 7 HERZ, 7 PIK, 7 KARO, 7 KREUZ
     * @param cardValues - the value of the card
     * @return - a list of cards with all combinations
     */
    List<Card> getCards(CardValue... cardValues);

    /**
     * The method mixes all cards randomly.
     * @param cards - a list of cards
     */
    void mixCards(List<Card> cards);

    /**
     * The method sorts all cards.
     * @param cards - a list of cards
     */
    void sortCards(List<Card> cards);
}
