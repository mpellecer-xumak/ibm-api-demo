package com.wdm.configuration.api.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import com.wdm.configuration.api.exception.HttpConflictException;
import com.wdm.configuration.api.exception.HttpNotFoundException;
import com.wdm.configuration.api.response.ErrorResponse;

@ControllerAdvice(annotations = RestController.class)
public class HttpClientErrorExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(value = HttpConflictException.class)
    public ErrorResponse handleException(final HttpConflictException ex) {
        return getErrorResponse(ex);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = HttpNotFoundException.class)
    public ErrorResponse handleException(final HttpNotFoundException ex) {
        return getErrorResponse(ex);
    }

    private ErrorResponse getErrorResponse(final HttpClientErrorException ex) {
        return ErrorResponse.builder()
                .code(ex.getRawStatusCode())
                .message(ex.getMessage())
                .build();
    }

}
