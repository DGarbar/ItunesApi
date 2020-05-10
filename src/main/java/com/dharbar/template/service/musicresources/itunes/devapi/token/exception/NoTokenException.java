package com.dharbar.template.service.musicresources.itunes.devapi.token.exception;

public class NoTokenException extends Exception {
    public NoTokenException() {
        super();
    }

    public NoTokenException(String message) {
        super(message);
    }

    public NoTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
