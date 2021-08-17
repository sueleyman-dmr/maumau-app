package de.htwberlin.maumau.player_management.export;

import de.htwberlin.maumau.card_management.export.Stack;
import de.htwberlin.maumau.card_management.export.EmptyStackException;

import java.util.List;

/**
 * This interface comprises the methods for a player service.
 * @author Süleyman Demir
 */
public interface PlayerService {

    /**
     * The method is creating certain player for a game.
     * @param playerAmount - player amount for a game.
     * @return - list of all created players
     */
    List<Player> createPlayer(int playerAmount);

    /**
     * The method returns the index of a specified player.
     * @param playerList - the list of the players
     * @param player - the explicit player
     * @return - the index of the player in list
     */
    int getPlayerIndexInList(List<Player> playerList, Player player);

    /**
     * The method distributes cards to all players in the list
     * @param stack - the stack of the game
     * @param playerList - list of the players
     * @param cardsPerPlayer - amount of cards per player
     * @throws EmptyStackException
     */
    void distributeCards(Stack stack, List<Player> playerList, int cardsPerPlayer) throws EmptyStackException;

    /**
     * The method transfers the cards of the player to the stack. Is needed in situations like, if the player surrendered.
     * @param stack
     * @param player
     */
    void playerCardsToStack(Stack stack, Player player);

    /**
     * The method adjusts the index of the players, because after a player surrendered all indexes are +1 and it need to be adapted to before.
     * @param playerList
     */
    void adjustPlayerIndex(List<Player> playerList);
}
