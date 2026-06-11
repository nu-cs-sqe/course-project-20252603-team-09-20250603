package domain;

public class IllegalActionException extends LocalizedDomainException {
    public IllegalActionException(DomainErrorKey errorKey, Object... messageArgs) {
        super(errorKey, messageArgs);
    }
}
