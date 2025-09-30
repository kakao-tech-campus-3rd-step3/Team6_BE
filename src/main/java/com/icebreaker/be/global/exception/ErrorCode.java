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
    USER_NOT_AUTHENTICATED("사용자가 인증되지 않았습니다.", HttpStatus.UNAUTHORIZED),

    //ROOM ERROR
    ROOM_NOT_FOUND("방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ROOM_ALREADY_EXISTS("방이 이미 존재합니다.", HttpStatus.CONFLICT),
    WAITING_ROOM_NOT_FOUND("대기방을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    WAITING_ROOM_FULL("대기방이 가득 찼습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_ROOM_JOIN("이미 방에 가입되어있습니다.", HttpStatus.CONFLICT),
    ROOM_CAPACITY_EXCEEDED("방 정원을 초과했습니다.", HttpStatus.BAD_REQUEST),
    INVALID_STAGE_VALUE("유효하지 않은 스테이지 값입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_IN_ROOM("사용자가 방에 속해있지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_STAGE_EVENT_TYPE_VALUE("유효하지 않은 스테이지 이벤트 타입 값입니다.", HttpStatus.BAD_REQUEST),
    INVALID_STAGE_EVENT_TYPE("유효하지 않은 스테이지 이벤트 타입입니다.", HttpStatus.BAD_REQUEST),
    INVALID_STAGE_TRANSITION("유효하지 않은 스테이지 전환입니다.", HttpStatus.BAD_REQUEST),
    ROOM_STAGE_NOT_FOUND("방의 현재 스테이지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    
    // QUESTION ERROR
    QUESTION_NOT_FOUND("질문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_QUESTION_TYPE("유효하지 않은 질문 타입입니다.", HttpStatus.BAD_REQUEST),

    // INTEREST ERROR
    INVALID_INTEREST_TYPE("유효하지 않은 관심사 타입입니다.", HttpStatus.BAD_REQUEST),

    INVALID_JWT_PAYLOAD("유효하지 않은 JWT 페이로드입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_TOKEN("유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT_SIGNATURE("유효하지 않은 JWT 서명입니다.", HttpStatus.UNAUTHORIZED),

    // DEFAULT ERROR
    INVALID_REQUEST("유효하지 않은 요청입니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus status;
}
