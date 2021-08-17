package de.htwberlin.maumau.card_management.export;

/**
 * CardNotAllowedActivityException.java<br>
 * The exception is thrown if a card is not allowed for an activity like "play".
 * @author Süleyman Demir
 */
public class CardNotAllowedActivityException extends Exception{

    public CardNotAllowedActivityException(String message) {
        super(message);
    }

    public CardNotAllowedActivityException(Throwable throwable) {
        super(throwable);
    }
}
