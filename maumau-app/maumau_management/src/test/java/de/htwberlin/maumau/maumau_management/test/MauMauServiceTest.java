package de.htwberlin.maumau.maumau_management.test;

import de.htwberlin.maumau.card_management.export.CardNotAllowedActivityException;
import de.htwberlin.maumau.card_management.export.EmptyStackException;
import de.htwberlin.maumau.card_management.export.*;
import de.htwberlin.maumau.card_management.impl.CardServiceImpl;
import de.htwberlin.maumau.maumau_management.export.MauMauTechnicalException;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMau;
import de.htwberlin.maumau.maumau_management.export.MauMauPlayerSizeException;
import de.htwberlin.maumau.maumau_management.export.mauGame.MauMauService;
import de.htwberlin.maumau.maumau_management.export.PlayerNotAllowedActivityException;
import de.htwberlin.maumau.maumau_management.impl.mauGame.MauMauServiceImpl;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.player_management.export.PlayerService;
import de.htwberlin.maumau.player_management.impl.PlayerServiceImpl;
import de.htwberlin.maumau.regel_management.export.RuleService;
import de.htwberlin.maumau.regel_management.impl.RuleServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * The class MauMauServiceTest is a JUnit-Test for testing the interface MauMauService.
 * @author Süleyman Demir
 */
public class MauMauServiceTest {
    private MauMauService mauMauService;
    private RuleService ruleService;
    private final int maxPlayerAmount = 5;
    private final int cardsPerPlayer = 4;

    /**
     * Sets up services which are necessary for the following tests
     *
     */
    @Before
    public void setUp(){
        CardService cardService = new CardServiceImpl();
        ruleService = new RuleServiceImpl();
        PlayerService playerService = new PlayerServiceImpl();

        MauMauServiceImpl mauMauServiceI = new MauMauServiceImpl(ruleService, cardService, playerService);

        this.mauMauService = mauMauServiceI;
    }


    /**
     * This method tests if a MauMau object, which is a game, can be created.
     * Also this method checks if the created maumau game is properly created.
     * That means if it is not null.
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testCreateGame() throws MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        assertNotNull(maumau);
    }
    
    @Test(expected = MauMauTechnicalException.class)
    public void testCreateNullGame() throws MauMauTechnicalException, PlayerNotAllowedActivityException {
    	MauMau maumau = null;
    	Player p = new Player("Player:"+1);
    	mauMauService.isTurnPossible(maumau, p);
    }

    /**
     * This method tests if the MauMauPlayerSizeException is thrown if
     * too many players are added to the game.
     * @throws MauMauPlayerSizeException
     */
    @Test(expected = MauMauPlayerSizeException.class)
    public void testAddToMuchPlayerToGame() throws MauMauPlayerSizeException, EmptyStackException {
     //   List<Player> playerList = playerService.createPlayer(playerAmount);

        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 5, 0,cardsPerPlayer, true);
        Player p = new Player("Player-"+5);

        mauMauService.addPlayerToGame(maumau, p);
    }

    /**
     * This method tests if players are properly added to the game. The players are being created
     * inside the "createGame method". There is a call from the playerService to creating the player.
     * Then we get the playerlist size and we are comparing/checking the sizes
     * it if it is what is expected.
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testAddPlayerToGame() throws MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        maumau.getPlayerList().remove(2);
        int expected = 2;
        /*
         * erstmal einen Spieler entfernen, da sonst maximaler Spieleranzahl des Spiels überschritten wäre, wenn
         * man einen Spieler hinzufügen möchte.
         */
        assertEquals(expected, maumau.getPlayerList().size());

        Player p5 = new Player("Player: " + 5);
        mauMauService.addPlayerToGame(maumau, p5);
        expected= 3;
        assertEquals(expected, maumau.getPlayerList().size());
    }

    /**
     * This method tests if a turn of
     * any kind is allowed or if an exception is thrown.
     * @throws MauMauPlayerSizeException
     * @throws PlayerNotAllowedActivityException
     * @throws MauMauTechnicalException
     */
    @Test
    public void testIsTurnPossible() throws MauMauPlayerSizeException, PlayerNotAllowedActivityException, MauMauTechnicalException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Player p1 = maumau.getPlayerList().get(0);
        maumau.setActualPlayer(p1);

        if(ruleService.isPlayersTurn(maumau.getActualPlayer(), p1)) {
            assertTrue(mauMauService.isTurnPossible(maumau, p1));
        }
    }

    /**
     * This method tests if a player which is currently in the game
     * can surrender and quit the game, after that we are checking the playerSize
     * after surrendering.
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testSurrenderGame() throws MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Player p1 = maumau.getPlayerList().get(0);
        int playerSize = maumau.getPlayerList().size();
        mauMauService.endGame(maumau, p1);
        int playerSizeAfterSurrender = maumau.getPlayerList().size();
        assertEquals(playerSize-1, playerSizeAfterSurrender);

    }

    /**
     * This method tests if the player mau call is properly
     * executed, so that the player can play their last card.
     * @throws MauMauPlayerSizeException
     * @throws PlayerNotAllowedActivityException
     * @throws MauMauTechnicalException
     */
    @Test
    public void testSayMau() throws MauMauPlayerSizeException, PlayerNotAllowedActivityException, MauMauTechnicalException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0, cardsPerPlayer, true);
        Player p1 = maumau.getPlayerList().get(0);
        maumau.setActualPlayer(p1);
        if(ruleService.mustCallMau(p1)) {
            if(ruleService.isPlayersTurn(maumau.getActualPlayer(), p1)) {
                mauMauService.sayMau(maumau, p1);
                assertTrue(maumau.getMauPlayer().get(p1.getPlayerIndex()));
            }
        }
    }

    /**
     * This method tests if the player can play a card and
     * and throws exceptions if wrong color, value etc. is trying to play
     * a card they are not allowed to.
     * @throws MauMauPlayerSizeException
     * @throws PlayerNotAllowedActivityException
     * @throws CardNotAllowedActivityException
     * @throws MauMauTechnicalException
     */
    @Test
    public void testPlayCard() throws MauMauPlayerSizeException, PlayerNotAllowedActivityException, CardNotAllowedActivityException, MauMauTechnicalException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Player p1 = maumau.getPlayerList().get(0);
        maumau.setActualPlayer(p1);

        Card discoveredCard = new Card(Color.PIK, CardValue.ZEHN);
        Card playerCard = new Card(Color.PIK, CardValue.NEUN);
        p1.getPlayerCards().add(playerCard);
        maumau.getDeck().setDiscoveredCard(discoveredCard);
        if(ruleService.isPlayersTurn(maumau.getActualPlayer(), p1)) {
            if (ruleService.isCardPlayable(maumau.getDeck(), playerCard, null, null)) {
                mauMauService.playCard(maumau, p1, playerCard);
                assertEquals(playerCard,
                        maumau.getDeck().getDiscoveredCard());
            }
        }
    }

    /**
     * Allowed player draws card activity is tested
     * @throws PlayerNotAllowedActivityException
     * @throws MauMauTechnicalException
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testDrawCardWithAllowedActivity() throws PlayerNotAllowedActivityException, MauMauTechnicalException, MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Player p1 = maumau.getPlayerList().get(0);
        maumau.setActualPlayer(p1);
        if (ruleService.isPlayersTurn(maumau.getActualPlayer(), p1)) {
            int playersCard = maumau.getActualPlayer().getPlayerCards().size();
            mauMauService.drawCard(maumau, maumau.getActualPlayer());
            int playersCardAfterDraw = p1.getPlayerCards().size();
            assertNotEquals(playersCard, playersCardAfterDraw);
        }
    }

    /**
     * Not allowed player draws card activity is tested
     * @throws PlayerNotAllowedActivityException
     * @throws MauMauPlayerSizeException
     * @throws MauMauTechnicalException
     */
    @Test(expected = PlayerNotAllowedActivityException.class)
    public void testDrawCardWithNotAllowedActivity() throws PlayerNotAllowedActivityException, MauMauPlayerSizeException, MauMauTechnicalException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Player p1 = maumau.getPlayerList().get(0);
        Player p2 = maumau.getPlayerList().get(1);
        maumau.setActualPlayer(p2);
        mauMauService.drawCard(maumau, p1);
    }

    /**
     * This method tests when a player wants to choose a
     * compulsory color for the next turn
     * @throws MauMauPlayerSizeException
     * @throws PlayerNotAllowedActivityException
     * @throws MauMauTechnicalException
     */
    @Test
    public void testChooseColor() throws MauMauPlayerSizeException, PlayerNotAllowedActivityException, MauMauTechnicalException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Card bube = new Card(Color.KREUZ, CardValue.BUBE);
        Player p1 = maumau.getPlayerList().get(0);
        maumau.setActualPlayer(p1);
        maumau.getDeck().setDiscoveredCard(bube);
        maumau.setNeedCompulsoryColor(true);
        if (ruleService.isPlayersTurn(maumau.getActualPlayer(), p1)) {
            mauMauService.chooseColor(maumau, p1, Color.KARO);
            assertEquals(Color.KARO, maumau.getCompulsoryColor());
        }
    }

    /**
     * Tests if first card of deck is the correct card,
     * which is discovered
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testFirstCardFromStackToDeck() throws MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        Card card = maumau.getStack().getStackCards().get(0);
        mauMauService.firstCardFromStackToDeck(maumau);
        assertEquals(card, maumau.getDeck().getDiscoveredCard());
    }

    /**
     *Tests if player is correctly punished. This method punishes the player
     *first, then counts his card count with the added
     *penalty cards and compares if it is correct.
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testPunishPlayer() throws MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount,3, 0, cardsPerPlayer, true);

        Player p1 = maumau.getPlayerList().get(0);
        p1.setNumberOfPenaltyCards(4);
        maumau.setActualPlayer(p1);

        int cardSizeBefore = maumau.getActualPlayer().getPlayerCards().size();
        mauMauService.punishPlayer(maumau, p1);
        int cardSizeAfter = maumau.getActualPlayer().getPlayerCards().size();
        assertEquals(cardSizeBefore+4, cardSizeAfter);
    }

    /**
     * This method tests if the deck of discovered cards is correctly added to the stack.
     * @throws MauMauPlayerSizeException
     */
    @Test
    public void testTransferDeckCardsToStack() throws MauMauPlayerSizeException, EmptyStackException {
        MauMau maumau = mauMauService.createGame(maxPlayerAmount, 3, 0,cardsPerPlayer, true);
        maumau.getDeck().getDeckCards().addAll(maumau.getStack().getStackCards());

        int deckCardSize = maumau.getDeck().getDeckCards().size();
        int stackSize = maumau.getStack().getStackSize();

        mauMauService.deckCardsToStack(maumau.getStack(), maumau.getDeck());
        int stackSizeAfterTransfer = maumau.getStack().getStackSize();

        assertEquals(1, maumau.getDeck().getDeckCards().size());
        assertEquals(stackSize+deckCardSize-1, stackSizeAfterTransfer);


    }


}
