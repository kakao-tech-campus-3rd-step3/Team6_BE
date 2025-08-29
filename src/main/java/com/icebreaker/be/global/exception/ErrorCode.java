package com.icebreaker.be.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // USER ERROR
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_MBTI_TYPE("유효하지 않은 MBTI 타입입니다.", HttpStatus.BAD_REQUEST),

    // INTEREST ERROR
    INTEREST_NOT_FOUND("관심사를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    INVALID_REQUEST("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;
}
