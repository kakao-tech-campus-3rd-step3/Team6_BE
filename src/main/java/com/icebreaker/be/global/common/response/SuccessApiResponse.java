package com.icebreaker.be.global.common.response;

import lombok.Getter;

@Getter
public class SuccessApiResponse<T> extends ApiResponse<T> {

    private SuccessApiResponse(String message, T data) {
        super(true, message, data);
    }

    public static <T> SuccessApiResponse<T> of(String message, T data) {
        return new SuccessApiResponse<>(message, data);
    }

    public static SuccessApiResponse<Void> of(String message) {
        return new SuccessApiResponse<>(message, null);
    }
}
