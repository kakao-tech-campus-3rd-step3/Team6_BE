package com.icebreaker.be.domain.question;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuestionType {
    COMMON("공통"),
    PERSONAL("개인");

    private final String value;

    public static QuestionType fromString(String questionType) {
        return Arrays.stream(values())
                .filter(t -> t.value != null && t.value.equals(questionType))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_QUESTION_TYPE));
    }
}
