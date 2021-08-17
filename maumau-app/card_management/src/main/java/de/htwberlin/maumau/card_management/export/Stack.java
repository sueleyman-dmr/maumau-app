package de.htwberlin.maumau.card_management.export;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stack.java<br>
 * The class stack comprises the function in a card game.
 * @author Süleyman Demir
 */
@Embeddable
public class Stack implements Serializable {

    private List<Card> stackCards;

    public Stack() {
        this.stackCards = new ArrayList<>();
    }

    /**
     * Constructor of the class Stack
     * @param cards - a list of cards
     */
    public Stack(List<Card> cards) {
        this.stackCards = new ArrayList<>(cards);
    }

    /**
     * The method adds a list of cards to the field stackCards (to the Stack)
     * @param cards - a list of cards
     */
    public void addCardsToStack(List<Card> cards) {
        this.stackCards.addAll(cards);
    }

    /**
     * The method returns a card, which is on the stack and removes it then.
     * If the stack is empty, the method throws an EmptyStackException
     * @return - the drawn card
     */
    public Card drawCard() throws EmptyStackException {
        if(this.stackCards.isEmpty()) {
            throw new EmptyStackException("Not enough cards on stack.");
        }
        return this.stackCards.remove(0);
    }

    /**
     * The method returns the count of cards on the stack.
     * @return - int stack size
     */
    public int getStackSize() {
        return this.getStackCards().size();
    }

    /**
     * The method returns a list of cards on the stack.
     * @return list of all cards on the stack.
     */
    public List<Card> getStackCards() {
        return stackCards;
    }

    public void setStackCards(List<Card> stackCards) {
        this.stackCards = stackCards;
    }
}
