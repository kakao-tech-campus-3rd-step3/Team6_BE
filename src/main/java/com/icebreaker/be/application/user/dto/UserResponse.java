package com.icebreaker.be.application.user.dto;

import com.icebreaker.be.domain.interest.InterestType;
import com.icebreaker.be.domain.user.MbtiType;

public record UserResponse(
        Long id,
        String name,
        String phone,
        Integer age,
        String[] interests,
        MbtiType mbti,
        String introduction
) {

}