package com.icebreaker.be.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ApiResponse<T> {

    private final boolean isSuccess;
    private final String message;
    @JsonInclude(Include.NON_NULL)
    private final T data;
}
