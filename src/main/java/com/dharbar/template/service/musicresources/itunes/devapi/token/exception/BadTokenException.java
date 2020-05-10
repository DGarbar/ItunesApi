package com.dharbar.template.service.musicresources.itunes.devapi.token.exception;

public class BadTokenException extends Exception {
    public BadTokenException() {
        super();
    }

    public BadTokenException(String message) {
        super(message);
    }

    public BadTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
