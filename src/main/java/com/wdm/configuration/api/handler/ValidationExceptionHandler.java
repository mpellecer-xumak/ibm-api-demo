package com.wdm.configuration.api.handler;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.wdm.configuration.api.response.ValidationErrorResponse;
import com.wdm.configuration.api.response.ValidationErrorResponse.ValidationField;

@ControllerAdvice(annotations = RestController.class)
public class ValidationExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ValidationErrorResponse handleValidationException(final MethodArgumentNotValidException ex) {
        final List<ValidationField> validationErrors = getValidations(ex.getBindingResult().getFieldErrors());
        return getValidationErrorResponse(validationErrors);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ValidationErrorResponse handleValidationException(final ConstraintViolationException ex) {
        List<ValidationField> validationErrors = ex.getConstraintViolations().stream()
                .map(cv -> new ValidationField(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());
        return getValidationErrorResponse(validationErrors);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public ValidationErrorResponse handleValidationException(final BindException ex) {
        final List<ValidationField> validationErrors = getValidations(ex.getBindingResult().getFieldErrors());
        return getValidationErrorResponse(validationErrors);
    }

    private ValidationErrorResponse getValidationErrorResponse(final List<ValidationField> validationErrors) {
        final ValidationErrorResponse response = new ValidationErrorResponse(validationErrors);
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("The payload is not correct or is missing some information");
        return response;
    }

    private List<ValidationField> getValidations(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fe -> new ValidationField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
    }

}
