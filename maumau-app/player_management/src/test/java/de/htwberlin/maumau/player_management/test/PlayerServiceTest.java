package de.htwberlin.maumau.player_management.test;

import de.htwberlin.maumau.card_management.export.EmptyStackException;
import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.CardService;
import de.htwberlin.maumau.card_management.export.Stack;
import de.htwberlin.maumau.card_management.impl.CardServiceImpl;
import de.htwberlin.maumau.player_management.export.Player;
import de.htwberlin.maumau.player_management.export.PlayerService;
import de.htwberlin.maumau.player_management.impl.PlayerServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The class PlayerServiceTest tests the methods of the interface PlayerService
 * @author Süleyman Demir
 */
public class PlayerServiceTest {


    private PlayerService playerService;
    private CardService cardService;

    @Before
    public void setUp(){
        playerService = new PlayerServiceImpl();
        cardService = new CardServiceImpl();
    }

    /**
     * The test checks, if there, for example, are 5 players created.
     */
    @Test
    public void testCreatePlayer() {
        //Arrange
        int playerAmount = 5;

        //Act
        List<Player> playerList = playerService.createPlayer(playerAmount);

        //Assert
        assertEquals(playerAmount, playerList.size());
    }

    /**
     * The tests checks, if the method getPlayerIndexInList returns the right index of the player.
     */
    @Test
    public void testGetPlayerIndexInList() {
        int playerAmount = 5;
        List<Player> playerList = playerService.createPlayer(playerAmount);
        Player p1 = playerList.get(0);
        int playerIndex = playerService.getPlayerIndexInList(playerList, p1);

        assertEquals(p1.getPlayerIndex(), playerIndex);
    }

    /**
     * The test checks, if all player got the same amount of cards and if the size of the stack is getting smaller after
     * the distribution
     * @throws Exception
     */
    @Test
    public void testDistributeCards() throws EmptyStackException {
        List<Card> maumauCards = cardService.createCards();
        List<Player> playerList = playerService.createPlayer(3);

        Stack stack = new Stack(maumauCards);
        int cardsPerPlayer = 5;

        playerService.distributeCards(stack, playerList, cardsPerPlayer);

        for (int i = 0; i < playerList.size(); i++) {
            assertEquals(cardsPerPlayer, playerList.get(i).getPlayerCards().size());
        }
        int expected = playerList.size() * cardsPerPlayer;
        int actual = maumauCards.size() - stack.getStackSize();
        assertEquals(expected, actual);
    }
    
    @Test(expected = EmptyStackException.class)
    public void testDistributeEmptyCards() throws EmptyStackException {
    	List<Player> playerList = playerService.createPlayer(3);

        Stack stack = new Stack();
        int cardsPerPlayer = 5;
        playerService.distributeCards(stack, playerList, cardsPerPlayer);
    }
}
