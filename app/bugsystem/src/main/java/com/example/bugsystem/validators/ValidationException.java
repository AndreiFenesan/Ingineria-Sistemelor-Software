package com.example.bugsystem.validators;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
