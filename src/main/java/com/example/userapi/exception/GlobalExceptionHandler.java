package com.example.userapi.exception;

import com.example.userapi.model.Country;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleEnumError(InvalidFormatException ex) {
        return Map.of(
                "error", "Unsupported country. Allowed: " + Arrays.toString(Country.values())
        );
    }
}