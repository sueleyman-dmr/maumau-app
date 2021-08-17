package de.htwberlin.maumau.card_management.export;

/**
 * EmptyStackException.java<br>
 * The exception is thrown if on the stack are no more cards.
 * @author Süleyman Demir
 */
public class EmptyStackException extends Exception{

    public EmptyStackException(String message) {
        super(message);
    }

    public EmptyStackException(Throwable throwable) {
        super(throwable);
    }
}
