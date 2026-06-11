package domain;

import java.util.Arrays;

public abstract class LocalizedDomainException extends RuntimeException {
    private final DomainErrorKey errorKey;
    private final Object[] messageArgs;

    protected LocalizedDomainException(DomainErrorKey errorKey, Object... messageArgs) {
        super(errorKey.key());
        this.errorKey = errorKey;
        this.messageArgs = messageArgs == null ? new Object[0] : Arrays.copyOf(messageArgs, messageArgs.length);
    }

    public DomainErrorKey getErrorKey() {
        return errorKey;
    }

    public String getMessageKey() {
        return errorKey.key();
    }

    public Object[] getMessageArgs() {
        return Arrays.copyOf(messageArgs, messageArgs.length);
    }
}
