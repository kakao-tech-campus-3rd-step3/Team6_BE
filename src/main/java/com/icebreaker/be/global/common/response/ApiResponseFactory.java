package com.icebreaker.be.global.common.response;

import com.icebreaker.be.global.exception.ErrorCode;

public class ApiResponseFactory {

    public static SuccessApiResponse<Void> success(String message) {
        return SuccessApiResponse.of(message);
    }

    public static <T> SuccessApiResponse<T> success(T data, String message) {
        return SuccessApiResponse.of(message, data);
    }

    public static ErrorApiResponse error(ErrorCode errorCode) {
        return ErrorApiResponse.of(errorCode.getMessage(), errorCode.name());
    }
}