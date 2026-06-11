package domain;

/**
 * Custom exception to handle illegal moves during the
 * Catan initial placement phase.
 */
public class IllegalPlacementException extends LocalizedDomainException {

    public IllegalPlacementException(DomainErrorKey errorKey, Object... messageArgs) {
        super(errorKey, messageArgs);
    }
}
