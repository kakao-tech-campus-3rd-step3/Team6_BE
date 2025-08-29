package com.icebreaker.be.fixture;

import com.icebreaker.be.application.user.dto.UserResponse;
import com.icebreaker.be.domain.interest.InterestType;
import com.icebreaker.be.domain.user.MbtiType;
import java.util.List;

public class UserResponseFixture {

    public static UserResponse defaultUserResponse() {
        return buildUserResponse(1L, "홍길동", "01012345678", 25, new String[] { "도서", "영화" }, MbtiType.INTJ, "안녕하세요");
    }

    public static List<UserResponse> sampleUserResponseList() {
        return List.of(
                defaultUserResponse(),
                buildUserResponse(2L, "김철수", "01087654321", 30, new String[] { "도서", "영화" }, MbtiType.ENFP, "반갑습니다")
        );
    }

    private static UserResponse buildUserResponse(Long id, String name, String phone, int age,
                                                  String[] interests, MbtiType mbti, String intro) {
        return new UserResponse(id, name, phone, age, interests, mbti, intro);
    }
}