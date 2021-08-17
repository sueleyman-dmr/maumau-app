package de.htwberlin.maumau.regel_management.export;

import de.htwberlin.maumau.card_management.export.Card;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The class GameMove describes the activity in game.
 * The class handles like a protocol. It is used as list in MauMau-Game to track the activities in game.
 * For example, to protocol, that 2-times a 7 is played, and the next player should be punished by drawing 4 cards.
 */
@Embeddable
public class GameMove implements Serializable {

    private int playerIndex;
    private Card playedCard;

    /**
     * Constructor of the class GameMove
     * @param playerIndex - the player index of the player, who did the game move.
     * @param playedCard - the played card or drawn card (then it is null)
     */
    public GameMove(int playerIndex, Card playedCard) {
        this.playerIndex = playerIndex;
        this.playedCard = playedCard;
    }

    /**
     * Returns the index of the player, who made this move .
     * @return
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * Sets the index of the player, who made this move.
     * @param playerIndex
     */
    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    /**
     * Returns the card of the player, who made this move.
     * @return
     */
    public Card getPlayedCard() {
        return playedCard;
    }

    /**
     * Sets the card of the player, who made this move.
     * @param playedCard
     */
    public void setPlayedCard(Card playedCard) {
        this.playedCard = playedCard;
    }

}
