package com.icebreaker.be.application.user.dto;

import com.icebreaker.be.domain.user.Interest;
import com.icebreaker.be.domain.user.MbtiType;
import java.util.Set;

public record UserResponse(
        Long id,
        String name,
        String phone,
        Integer age,
        MbtiType mbti,
        String introduction,
        Set<Interest> interests
) {

}