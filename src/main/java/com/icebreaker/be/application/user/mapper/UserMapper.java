package com.icebreaker.be.application.user.mapper;

import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.domain.user.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getAge(),
                user.getMbti(),
                user.getIntroduction()
        );
    }
}
