package com.nat.nexastream.exceptions;

public class TaskTimeoutException extends Exception{

    public TaskTimeoutException(String message) {
        super(message);
    }

    public TaskTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
