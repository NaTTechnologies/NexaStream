package com.nat.nexastream.exceptions;

public class TemporaryFailureException extends Exception {

    public TemporaryFailureException(String message) {
        super(message);
    }

    public TemporaryFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
