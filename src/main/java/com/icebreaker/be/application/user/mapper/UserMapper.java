package com.icebreaker.be.application.user.mapper;

import com.icebreaker.be.application.user.dto.CreateUserCommand;
import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.domain.user.Interest;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import java.util.Set;
import java.util.stream.Collectors;
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
                user.getIntroduction(),
                user.getInterestsEnum()
        );
    }

    public static User toEntity(CreateUserCommand cmd) {
        Set<Interest> interests = cmd.interests().stream()
                .map(Interest::fromKoreanString)
                .collect(Collectors.toSet());

        User user = User.builder()
                .name(cmd.name())
                .phone(cmd.phone())
                .age(cmd.age())
                .mbti(MbtiType.fromString(cmd.mbtiValue()))
                .introduction(cmd.introduction())
                .build();

        interests.forEach(user::addInterest);

        return user;
    }
}
