package com.wdm.configuration.api.exception;

public class HttpClientIntegrationException extends IntegrationException {
    private static final long serialVersionUID = -8664183241772909919L;

    public HttpClientIntegrationException(final String message) {
        super(message);
    }

    public HttpClientIntegrationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
