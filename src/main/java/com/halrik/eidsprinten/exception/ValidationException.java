package com.halrik.eidsprinten.exception;

public class ValidationException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }

}
