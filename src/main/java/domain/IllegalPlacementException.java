package domain;

/**
 * Custom exception to handle illegal moves during the
 * Catan initial placement phase.
 */
public class IllegalPlacementException extends RuntimeException {

    public IllegalPlacementException(String message) {
        super(message);
    }
}