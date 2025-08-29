package com.icebreaker.be.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;
import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Interest {
    MUSIC("음악"),
    MOVIES("영화"),
    SPORTS("스포츠"),
    TRAVEL("여행"),
    FOOD("음식"),
    TECHNOLOGY("기술"),
    GAMING("게임"),
    BOOKS("도서"),
    ART("예술"),
    PHOTOGRAPHY("사진"),
    FASHION("패션"),
    FITNESS("피트니스"),
    HEALTH("건강"),
    OUTDOORS("야외활동"),
    HIKING("등산"),
    CYCLING("자전거"),
    RUNNING("달리기"),
    SWIMMING("수영"),
    COOKING("요리"),
    PETS("반려동물");

    private static final Map<String, Interest> KOREAN_TO_ENUM;

    static {
        Map<String, Interest> map = new HashMap<>();
        for (Interest type : values()) {
            map.put(type.getDisplayName(), type);
        }
        KOREAN_TO_ENUM = Collections.unmodifiableMap(map);
    }

    private final String displayName;

    public static Interest fromKoreanString(String value) {
        Interest interest = KOREAN_TO_ENUM.get(value);
        if (interest == null) {
            throw new BusinessException(ErrorCode.INVALID_INTEREST_TYPE);
        }
        return interest;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
