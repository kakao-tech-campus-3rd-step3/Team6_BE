package com.icebreaker.be.global.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class ApiResponse<T> {

    private final boolean isSuccess;
    private final String message;
    @JsonInclude(Include.NON_NULL)
    private final T data;
}
