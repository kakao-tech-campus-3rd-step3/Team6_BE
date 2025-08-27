package com.icebreaker.be.domain.user;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;

public enum MbtiType {
    INTJ,
    INTP,
    ENTJ,
    ENTP,
    INFJ,
    INFP,
    ENFJ,
    ENFP,
    ISTJ,
    ISFJ,
    ESTJ,
    ESFJ,
    ISTP,
    ISFP,
    ESTP,
    ESFP;

    public static MbtiType fromString(String mbtiString) {
        try {
            return MbtiType.valueOf(mbtiString.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException(ErrorCode.INVALID_MBTI_TYPE);
        }
    }
}
