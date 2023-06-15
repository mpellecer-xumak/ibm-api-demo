package com.wdm.configuration.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class HttpNotFoundException extends HttpClientErrorException {
    private static final long serialVersionUID = 6438001278189568L;

    public HttpNotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
    
}
