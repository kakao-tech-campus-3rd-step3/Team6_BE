package com.icebreaker.be.domain.question;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;

public enum QuestionType {
    COMMON, PERSONAL;

    public static QuestionType fromString(String questionType) {
        if (questionType == null) {
            throw new BusinessException(ErrorCode.INVALID_QUESTION_TYPE);
        }

        if (questionType.equals("공통")) {
            return COMMON;
        } else if (questionType.equals("개인")) {
            return PERSONAL;
        } else {
            throw new BusinessException(ErrorCode.INVALID_QUESTION_TYPE);
        }
    }
}
