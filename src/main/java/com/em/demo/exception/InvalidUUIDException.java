package com.em.demo.exception;

public class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String message) {
        super(message);
    }
}
