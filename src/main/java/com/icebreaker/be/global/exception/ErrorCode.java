package com.icebreaker.be.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;
}
