package com.icebreaker.be.global.common.response;

import lombok.Getter;

@Getter
public class ErrorApiResponse extends ApiResponse<Void> {

    private final String errorCode;

    private ErrorApiResponse(String message, String errorCode) {
        super(false, message, null);
        this.errorCode = errorCode;
    }

    public static ErrorApiResponse of(String message, String errorCode) {
        return new ErrorApiResponse(message, errorCode);
    }
}