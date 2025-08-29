package com.icebreaker.be.domain.interest;

import com.icebreaker.be.global.exception.BusinessException;
import com.icebreaker.be.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum InterestType {
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

    private final String displayName;

    public static InterestType fromKoreanString(String value) {
        return Stream.of(InterestType.values())
                .filter(t -> t.getDisplayName().equals(value))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INTEREST_TYPE));
    }
}
