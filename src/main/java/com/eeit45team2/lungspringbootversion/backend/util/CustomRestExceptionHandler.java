package com.eeit45team2.lungspringbootversion.backend.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        final Map<String ,String> errors = new HashMap<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(),error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(),error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        return handleExceptionInternal(ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }
}
