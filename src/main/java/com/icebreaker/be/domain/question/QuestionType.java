package com.icebreaker.be.domain.question;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;

public enum QuestionType {
    COMMON("공통"),
    PERSONAL("개인");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public static QuestionType fromString(String questionType) {
        if (questionType == null) {
            throw new BusinessException(ErrorCode.INVALID_QUESTION_TYPE);
        }

        // value(): enum에 정의된 모든 상수를 배열형태로 반환
        for (QuestionType type : values()) {
            if (type.value.equals(questionType)) {
                return type;
            }
        }

        throw new BusinessException(ErrorCode.INVALID_QUESTION_TYPE);
    }

    public String getValue() {
        return value;
    }
}
