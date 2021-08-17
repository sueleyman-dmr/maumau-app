package de.htwberlin.maumau.maumau_management.export;

/**
 * The exception is thrown if a player does an move without authorization.
 * @author Süleyman Demir
 */
public class PlayerNotAllowedActivityException extends Exception{

    public PlayerNotAllowedActivityException(String msg) {
        super(msg);
    }

    public PlayerNotAllowedActivityException(Throwable throwable) {
        super(throwable);
    }
}

