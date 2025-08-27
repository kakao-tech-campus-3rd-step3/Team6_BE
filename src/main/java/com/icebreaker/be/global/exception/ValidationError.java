package com.icebreaker.be.global.exception;

public record ValidationError(
        String field,
        String message,
        Object rejectedValue
) {

}
