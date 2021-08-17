package de.htwberlin.maumau.maumau_management.export;

/**
 * The exception is thrown if more player wants to be added to the game then the maximal player count.
 */
public class MauMauPlayerSizeException extends Exception{

    public MauMauPlayerSizeException(String msg) {
        super(msg);
    }

    public MauMauPlayerSizeException(Throwable throwable) {
        super(throwable);
    }
}
