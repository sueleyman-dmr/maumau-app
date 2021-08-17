package de.htwberlin.maumau.regel_management.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.CardValue;
import de.htwberlin.maumau.card_management.export.Color;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.regel_management.export.Deck;
import de.htwberlin.maumau.regel_management.export.GameMove;
import de.htwberlin.maumau.regel_management.export.RuleService;
import de.htwberlin.maumau.regel_management.impl.RuleServiceImpl;

/**
 * The class RuleServiceTest is testing the "rules" of the interface RuleService
 */
public class RuleServiceTest {

    private RuleService ruleService;

    @Before
    public void setUp() {
        ruleService = new RuleServiceImpl();
    }

    /**
     * The test, checks if it is players turn.
     */
    @Test
    public void testIsPlayersTurn() {
        Player notActualPlayer = new Player();
        Player actualPlayer = new Player();

        notActualPlayer.setPlayerIndex(0);
        actualPlayer.setPlayerIndex(0);

        boolean isTurn = ruleService.isPlayersTurn(notActualPlayer, actualPlayer);
        assertTrue(isTurn);
    }

    /**
     * The tests, checks if the card is playable in game.
     */
    @Test
    public void isCardPlayable() {
        Deck deck = new Deck();
        GameMove lastMove = new GameMove(0, null);

        // checks the value of the card
        deck.setDiscoveredCard( new Card(Color.KARO, CardValue.ZEHN));
        Card wantedCard = new Card(Color.HERZ, CardValue.ZEHN);
        assertTrue(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        //checks value and color, expected is false
        wantedCard = new Card(Color.KREUZ, CardValue.NEUN);
        assertFalse(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        //checks the color of the card
        deck.setDiscoveredCard( new Card(Color.PIK, CardValue.DAME));
        wantedCard = new Card(Color.PIK, CardValue.SIEBEN);
        assertTrue(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        //checks jack as playable card all the time
        wantedCard = new Card(Color.KARO, CardValue.BUBE);
        assertTrue(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        //checks if jack is played on jack
        deck.setDiscoveredCard(new Card(Color.PIK, CardValue.BUBE));
        wantedCard = new Card(Color.HERZ, CardValue.BUBE);
        assertFalse(ruleService.isCardPlayable(deck, wantedCard, Color.HERZ, lastMove));

        deck.setDiscoveredCard(new Card(Color.KARO, CardValue.BUBE));
        wantedCard = new Card(Color.KREUZ, CardValue.ACHT);
        assertTrue(ruleService.isCardPlayable(deck, wantedCard, Color.KREUZ, lastMove));

        deck.setDiscoveredCard(new Card(Color.PIK, CardValue.SIEBEN));
        wantedCard = new Card(Color.HERZ, CardValue.ZEHN);
        assertFalse(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        lastMove = new GameMove(0, new Card(Color.HERZ, CardValue.SIEBEN));
        deck.setDiscoveredCard(lastMove.getPlayedCard());
        wantedCard	= new Card(Color.HERZ	, CardValue.BUBE);
        assertFalse(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        lastMove = new GameMove(0, null);
        assertTrue(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

        lastMove = new GameMove(0, new Card(Color.PIK, CardValue.SIEBEN));
        deck.setDiscoveredCard(lastMove.getPlayedCard());
        wantedCard = new Card(Color.KREUZ, CardValue.SIEBEN);
        assertTrue(ruleService.isCardPlayable(deck, wantedCard, null, lastMove));

    }

    /**
     * The test checks if the player must call mau.
     */
    @Test
    public void testMustCallMau() {
        List<Card> playerCards = new ArrayList<>();
        Player player = new Player();
        player.setPlayerCards(playerCards);

        player.getPlayerCards().add(new Card(Color.PIK, CardValue.ASS));
        assertTrue(ruleService.mustCallMau(player));

        player.getPlayerCards().add(new Card(Color.PIK, CardValue.NEUN));
        assertTrue(ruleService.mustCallMau(player));

        player.getPlayerCards().add(new Card(Color.HERZ, CardValue.BUBE));
        assertFalse(ruleService.mustCallMau(player));
    }

    /**
     * The test checks, if the method returns the right number of mau call penalty.
     */
    @Test
    public void testGetMauCallPenalty() {
        int[] expectedPenaltys = new int[3];
        expectedPenaltys[1] = 2;

        int[] actualPenaltys = new int[3];
        actualPenaltys[0] = ruleService.getMauCallPenalty(false, false);
        actualPenaltys[1] = ruleService.getMauCallPenalty(true, false);
        actualPenaltys[2] = ruleService.getMauCallPenalty(true, true);

        assertArrayEquals(expectedPenaltys, actualPenaltys);
    }

    /**
     * The test checks if there must be chosen a color.
     */
    @Test
    public void testMustChooseColor(){
        Card chooseColorCard = new Card(Color.KREUZ, CardValue.BUBE);
        Card wrongCard = new Card(Color.PIK, CardValue.ACHT);

        assertTrue(ruleService.mustChooseColor(chooseColorCard));
        assertFalse(ruleService.mustChooseColor(wrongCard));
    }

    /**
     * The test checks, if the game direction change is working correctly.
     */
    @Test
    public void testGetGameDirection() {
        boolean gameDirectionClockwise = false;
        Card changeDirectionCard = new Card(Color.PIK, CardValue.NEUN);
        Card wrongCard = new Card(Color.KREUZ, CardValue.ZEHN);

        boolean[] expected = {true, false, true, true};

        gameDirectionClockwise = ruleService.getGameDirection(changeDirectionCard, gameDirectionClockwise);
        boolean gameDirectionAgainstClockwise = ruleService.getGameDirection(changeDirectionCard, gameDirectionClockwise);
        boolean gameDirectionAgainClockwise = ruleService.getGameDirection(changeDirectionCard, gameDirectionAgainstClockwise);
        gameDirectionAgainClockwise = ruleService.getGameDirection(wrongCard, gameDirectionAgainClockwise);

        boolean[] actual = {gameDirectionClockwise, gameDirectionAgainstClockwise, gameDirectionAgainClockwise, gameDirectionAgainClockwise};

        assertArrayEquals(expected, actual);

    }

    /**
     * The test checks, if the player change is right.
     */
    @Test
    public void testCheckPlayerChange() {

        Card card = new Card( Color.KARO, CardValue.ASS);
        Card card2 = new Card(Color.KARO, CardValue.ACHT);
        Card card3 = new Card(Color.PIK, CardValue.ACHT);

        boolean[] expected = {
                false,
                true,
                true
        };

        boolean[] actual = {ruleService.checkPlayerChange(card), ruleService.checkPlayerChange(card2), ruleService.checkPlayerChange(card3)};

        assertArrayEquals(expected, actual);
    }

    /**
     * The test checks, if the next player index is correct.
     */
    @Test
    public void testGetNextPlayerIndex() {
        boolean gameDirectionClockwise = true;
        boolean gameDirectionAgainstClockwise = false;
        int playerAmount = 3;

        int[] expected = {1, 2, 0, 1, 2, 0};
        int[] expectedAgainstClockwise = {1, 0, 2, 1, 0, 2};

        int[] actual = {
                ruleService.getNextPlayerIndex(gameDirectionClockwise, playerAmount, 0),
                ruleService.getNextPlayerIndex(gameDirectionClockwise, playerAmount, 1), //2
                ruleService.getNextPlayerIndex(gameDirectionClockwise, playerAmount, 2), //3
                ruleService.getNextPlayerIndex(gameDirectionClockwise, playerAmount, 0),
                ruleService.getNextPlayerIndex(gameDirectionClockwise, playerAmount, 1),
                ruleService.getNextPlayerIndex(gameDirectionClockwise, playerAmount, 2),
        };

        int[] actualAgainstClockwise = {ruleService.getNextPlayerIndex(gameDirectionAgainstClockwise, playerAmount, 2),
                ruleService.getNextPlayerIndex(gameDirectionAgainstClockwise, playerAmount, 1),
                ruleService.getNextPlayerIndex(gameDirectionAgainstClockwise, playerAmount, 0),
                ruleService.getNextPlayerIndex(gameDirectionAgainstClockwise, playerAmount, 2),
                ruleService.getNextPlayerIndex(gameDirectionAgainstClockwise, playerAmount, 1),
                ruleService.getNextPlayerIndex(gameDirectionAgainstClockwise, playerAmount, 0)};

        assertArrayEquals(expected, actual);
        assertArrayEquals(expectedAgainstClockwise, actualAgainstClockwise);
    }

    /**
     * The test checks, if the amount of penalty cards by 7 is correct returned.
     */
    @Test
    public void testGetPenaltyCardsAmountBy7() {
        List<GameMove> gameMoves = new ArrayList<>();
        Card penaltyCard = new Card(Color.KARO, CardValue.SIEBEN);
        Card normalCard = new Card(Color.KARO, CardValue.ZEHN);
        Card normalCard2 = new Card(Color.PIK, CardValue.ZEHN);

        int playerIndex = 0;


        gameMoves.add(new GameMove(playerIndex, normalCard));
        assertEquals(0, ruleService.getPenaltyCardsAmountBy7(gameMoves));
        gameMoves.add(new GameMove(playerIndex, penaltyCard));
        assertEquals(2, ruleService.getPenaltyCardsAmountBy7(gameMoves));
        gameMoves.add(new GameMove(playerIndex, penaltyCard));
        assertEquals(4, ruleService.getPenaltyCardsAmountBy7(gameMoves));
        gameMoves.add(new GameMove(playerIndex, penaltyCard));
        assertEquals(6, ruleService.getPenaltyCardsAmountBy7(gameMoves));
        gameMoves.add(new GameMove(playerIndex, normalCard2));
        assertEquals(0, ruleService.getPenaltyCardsAmountBy7(gameMoves));
    }

    /**
     * The test checks, if the method returns the right winner.
     */
    @Test
    public void testGetWinner() {
        List<Player> playerList = new ArrayList<>();
        Card card = new Card(Color.KREUZ, CardValue.NEUN);
        for(int i=1; i <= 3; i++) {
            Player player = new Player("Player: " + i);
            player.getPlayerCards().add(card);
            playerList.add(player);
        }

        //prüft, dass es keinen Gewinner noch gibt
        assertNull(ruleService.getWinner(playerList));

        //nun ist der Spieler winner der Gewinner aus der playerList
        Player winner = playerList.get(1);
        winner.getPlayerCards().clear();
        assertEquals(winner, ruleService.getWinner(playerList));
    }

}
