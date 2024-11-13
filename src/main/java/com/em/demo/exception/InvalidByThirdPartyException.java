package com.em.demo.exception;

public class InvalidByThirdPartyException extends RuntimeException {
    public InvalidByThirdPartyException(String message) {
        super(message);
    }
}
