package com.icebreaker.be.application.interest.mapper;

import com.icebreaker.be.application.interest.dto.InterestResponse;
import com.icebreaker.be.domain.interest.Interest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class InterestMapper {

    public static InterestResponse toResponse(Interest interest) {
        return new InterestResponse(
                interest.getId(),
                interest.getName()
        );
    }
}
