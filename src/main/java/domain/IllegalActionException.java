package domain;

/**
 * Thrown when a player attempts an invalid game action,
 * such as playing a development card out of turn order.
 */
public class IllegalActionException extends RuntimeException {
    public IllegalActionException(String message) {
        super(message);
    }
}