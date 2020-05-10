package com.dharbar.template.config;

import com.dharbar.template.service.userService.repo.exception.UserNotFoundException;
import lombok.Builder;
import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;

@Configuration
public class ExceptionHandlers {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ExceptionDetails notFoundExceptionHandler(UserNotFoundException e) {
        return ExceptionDetails.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ExceptionDetails illegalArgumentHandler(IllegalArgumentException e) {
        return ExceptionDetails.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
    }

    @Value
    @Builder
    public static class ExceptionDetails {
        Instant timestamp;
        String status;
        Integer statusCode;
        String message;
    }
}
