package com.icebreaker.be.global.exception;

import org.springframework.validation.FieldError;

public record ValidationError(
        String field,
        String message,
        Object rejectedValue
) {

    public static ValidationError of(FieldError error) {
        return new ValidationError(
                error.getField(),
                error.getDefaultMessage(),
                error.getRejectedValue()
        );
    }
}
