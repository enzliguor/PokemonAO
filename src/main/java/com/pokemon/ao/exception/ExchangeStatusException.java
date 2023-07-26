package com.pokemon.ao.exception;

public class ExchangeStatusException extends Exception {

    public ExchangeStatusException(String message) {
        super(message);
    }

    public ExchangeStatusException(String message, Throwable cause) {
        super(message, cause);
    }
}