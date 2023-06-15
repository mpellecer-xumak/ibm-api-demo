package com.wdm.configuration.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class HttpConflictException extends HttpClientErrorException {

    private static final long serialVersionUID = 7484964828348416L;

    public HttpConflictException(final String message) {
        super(HttpStatus.CONFLICT, message);
    }

}
