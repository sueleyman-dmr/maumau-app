package de.htwberlin.maumau.player_management.export;

import de.htwberlin.maumau.card_management.export.Card;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The class describes a player in a card game.
 * @author Süleyman Demir
 */

@Embeddable
public class Player implements Serializable {


    private Long playerId;

    private String name;
    private int playerIndex;
    private int numberOfPenaltyCards;
    private List<Card> playerCards;
    private boolean bot;

    /**
     * Constructor of class player.
     * @param name - The name of the player
     */
    public Player(String name) {
        super();
        this.name = name;
        this.playerCards = new ArrayList<>();
    }

    public Player() {

    }

    /**
     * Returns true/false if card is successfully got removed from the list of a player.
     * @param card - specified card
     * @return true or false for removed successfully
     */
    public boolean isCardRemoved(Card card){
        return this.getPlayerCards().remove(card);
    }

    /**
     * The method returns the name of the player.
     * @return String name of player
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The method returns the player id.
     * @return Long playerId
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * The method sets the player id.
     * @param playerId Long player id
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * The method sets the player index.
     * @param playerIndex int player index
     */
    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * The method returns the number of penalty cards.
     * @return int number of penalty cards
     */
    public int getNumberOfPenaltyCards() {
        return numberOfPenaltyCards;
    }

    /**
     * The method sets the number of the penalty cards for a punishment in game.
     * @param numberOfPenaltyCards - int number of penalty cards
     */
    public void setNumberOfPenaltyCards(int numberOfPenaltyCards) {
        this.numberOfPenaltyCards = numberOfPenaltyCards;
    }

    /**
     * The method returns a list of the player cards.
     * @return list of the player cards
     */
    public List<Card> getPlayerCards() {
        return playerCards;
    }

    /**
     * The method sets the player cards in list.
     * @param playerCards - the distributed cards.
     */
    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

}
