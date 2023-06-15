package com.wdm.configuration.api.exception;

public class IntegrationException extends Exception {
    private static final long serialVersionUID = -4384810610414308456L;

    public IntegrationException(final String message) {
        super(message);
    }

    public IntegrationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
