package de.htwberlin.maumau.regel_management.export;

import de.htwberlin.maumau.card_management.export.Card;
import de.htwberlin.maumau.card_management.export.Stack;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a deck on which cards are revealed or played by players.
 * @author Süleyman Demir
 *
 */

@Embeddable
public class Deck implements Serializable {

    private Card discoveredCard;

    private List<Card> deckCards;

    /**
     * The constructor of the class deck.
     */
    public Deck() {
        this.deckCards=new ArrayList<>();
    }

    /**
     * The method discovers the card which is placed/played on the deck from the stack or player.
     * @param card - specified card
     */
    public void discoverCard(Card card) {
        this.deckCards.add(card);
    }

    /**
     * The method returns the last discovered card on the deck.
     * @return the last discovered card
     */
    public Card getDiscoveredCard() {
        if(!this.deckCards.isEmpty()) {
            this.discoveredCard = this.deckCards.get(this.deckCards.size()-1);
        }
        return this.discoveredCard;
    }

    /**
     * The method transfers all cards to the stack, if there are to many.
     * It lets one card on the deck, to let the game move on.
     * @param stack - the stack
     */
    public void deckCardsToStack(Stack stack) {
        if (this.deckCards.size() < 2) {
            return;
        } else {
            Card discoveredCard = this.deckCards.remove(this.deckCards.size()-1);
            stack.addCardsToStack(this.deckCards);
            this.deckCards.clear();
            this.deckCards.add(discoveredCard);
        }
    }

    /**
     * The method sets the discovered card of the deck.
     * @param discoveredCard
     */
    public void setDiscoveredCard(Card discoveredCard) {
        this.discoveredCard = discoveredCard;
    }

    /**
     * The method returns all cards as a list on the deck.
     * @return - a list of cards on deck
     */
    public List<Card> getDeckCards() {
        return deckCards;
    }

}
