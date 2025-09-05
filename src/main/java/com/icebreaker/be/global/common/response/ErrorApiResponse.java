package com.icebreaker.be.global.common.response;

import lombok.Getter;

@Getter
public class ErrorApiResponse<T> extends ApiResponse<T> {

    private final String errorCode;

    private ErrorApiResponse(String message, String errorCode, T data) {
        super(false, message, data);
        this.errorCode = errorCode;
    }

    public static <T> ErrorApiResponse<T> of(String message, String errorCode, T data) {
        return new ErrorApiResponse<>(message, errorCode, data);
    }

    public static ErrorApiResponse<Void> of(String message, String errorCode) {
        return new ErrorApiResponse<>(message, errorCode, null);
    }
}
