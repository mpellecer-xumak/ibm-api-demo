package com.wdm.configuration.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class HttpBadRequestException extends HttpClientErrorException {

    private static final long serialVersionUID = -5250246368034816L;

    public HttpBadRequestException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
