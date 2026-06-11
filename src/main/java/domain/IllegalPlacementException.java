package domain;

public class IllegalPlacementException extends LocalizedDomainException {

    public IllegalPlacementException(DomainErrorKey errorKey, Object... messageArgs) {
        super(errorKey, messageArgs);
    }
}
