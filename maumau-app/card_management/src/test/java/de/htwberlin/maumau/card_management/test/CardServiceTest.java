package de.htwberlin.maumau.card_management.test;
import de.htwberlin.maumau.card_management.export.*;
import de.htwberlin.maumau.card_management.impl.CardServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * CardServiceTest.java<br>
 * The test is testing the methods of the the service {@link CardService}.
 * @author Süleyman Demir
 */
public class CardServiceTest {

    private CardService cardService;
    private final List<Card> exampleCards = new LinkedList<>();

    /**
     * The method initializes the cardService and creates some example game cards.
     */
    @Before
    public void setUp() {
        cardService = new CardServiceImpl();
        List<Card> allCards = cardService.createCards();
        for (Card c : allCards) {
            if(c.getColor().equals(Color.KARO)) {
                exampleCards.add(c);
                cardService.sortCards(exampleCards);
            }
        }
    }

    /**
     * The method tests if the game cards are created correctly.
     */
    @Test
    public void testCreateCards(){
        //Arrange
        List<Card> testCards = new LinkedList<>();

        //Act
        for (CardValue cw: CardValue.values()) {
            testCards.add(new Card(Color.KARO, cw));
        }

        //Assert
        assertEquals(testCards, exampleCards);
    }

    /**
     * The method tests if the correct combinations of a card are returned.
     */
    @Test
    public void testGetKarten() {

        List<Card> assCombination = cardService.getCards(CardValue.ASS);




        assertEquals(4, assCombination.size());
    }

    /**
     * The method tests if the cards are correctly mixed and then sorted.
     */
    @Test
    public void testMixCardsAndThenSortedCards() {
        Card[] copy = exampleCards.toArray(new Card[0]); //sortiert
        cardService.mixCards(exampleCards);
        Card[] mixedCards = exampleCards.toArray(new Card[0]); //gemischt

        cardService.sortCards(exampleCards);
        Card[] sortedCards = exampleCards.toArray(new Card[0]);

        assertFalse(Arrays.equals(copy, mixedCards));
        assertArrayEquals(copy, sortedCards);
    }

}
