package com.tanmay.helpdesk_backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource Not Found Exception
    @ExceptionHandler(
            ResourceNotFoundException.class)

    @ResponseStatus(HttpStatus.NOT_FOUND)

    public Map<String, String>
    handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "message",
                ex.getMessage());

        return error;
    }

    // Validation Exception
    @ExceptionHandler(
            MethodArgumentNotValidException.class)

    @ResponseStatus(HttpStatus.BAD_REQUEST)

    public Map<String, String>
    handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->

                    errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                    )
                );

        return errors;
    }

    @ExceptionHandler(
        UnauthorizedException.class)

       @ResponseStatus(HttpStatus.FORBIDDEN)

     public Map<String, String>
      handleUnauthorizedException(
        UnauthorizedException ex) {

    Map<String, String> error =
            new HashMap<>();

    error.put(
            "message",
            ex.getMessage());

    return error;
   }
   
}