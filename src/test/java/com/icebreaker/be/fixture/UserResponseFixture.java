package com.icebreaker.be.fixture;

import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.domain.user.Interest;
import com.icebreaker.be.domain.user.MbtiType;
import java.util.List;
import java.util.Set;

public class UserResponseFixture {

    public static UserResponse defaultUserResponse() {
        return buildUserResponse(
                1L,
                "홍길동",
                "01012345678",
                25,
                MbtiType.INTJ,
                "안녕하세요",
                Set.of(Interest.BOOKS, Interest.MOVIES)
        );
    }

    public static List<UserResponse> sampleUserResponseList() {
        return List.of(
                defaultUserResponse(),
                buildUserResponse(
                        2L,
                        "김철수",
                        "01087654321",
                        30,
                        MbtiType.ENFP,
                        "반갑습 니다",
                        Set.of(Interest.BOOKS, Interest.MOVIES)
                )
        );
    }

    private static UserResponse buildUserResponse(
            Long id,
            String name,
            String phone,
            int age,
            MbtiType mbti,
            String intro,
            Set<Interest> interests
    ) {
        return new UserResponse(id, name, phone, age, mbti, intro, interests);
    }
}