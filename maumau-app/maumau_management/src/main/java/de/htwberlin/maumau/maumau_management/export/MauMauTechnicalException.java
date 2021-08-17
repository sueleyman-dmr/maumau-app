package de.htwberlin.maumau.maumau_management.export;

/**
 * The exception is thrown when an technical problem occurs.
 * @author Sercan Sanli
 */
public class MauMauTechnicalException extends Exception{

    public MauMauTechnicalException(String message) {
        super(message);
    }

    public MauMauTechnicalException(Throwable throwable) {
        super(throwable);
    }
}
