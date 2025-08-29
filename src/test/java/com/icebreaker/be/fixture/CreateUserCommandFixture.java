package com.icebreaker.be.fixture;

import com.icebreaker.be.application.user.dto.CreateUserCommand;
import java.util.Set;

public class CreateUserCommandFixture {

    public static CreateUserCommand validCommand() {
        return buildCommand("홍길동", "01012345678", 25, Set.of("도서", "영화"), "INTJ", "안녕하세요");
    }

    public static CreateUserCommand invalidNameCommand() {
        return buildCommand("", "01012345678", 25, Set.of("도서", "영화"), "INTJ", "안녕하세요");
    }

    public static CreateUserCommand invalidPhoneCommand() {
        return buildCommand("홍길동", "123", 25, Set.of("도서", "영화"), "INTJ", "안녕하세요");
    }

    public static CreateUserCommand invalidAgeCommand() {
        return buildCommand("홍길동", "01012345678", -1, Set.of("도서", "영화"), "INTJ", "안녕하세요");
    }

    public static CreateUserCommand tooLongNameCommand() {
        return buildCommand("홍길동".repeat(10), "01012345678", 25, Set.of("도서", "영화"), "INTJ",
                "안녕하세요");
    }

    public static CreateUserCommand tooLongIntroductionCommand() {
        return buildCommand("홍길동", "01012345678", 25, Set.of("도서", "영화"), "INTJ",
                "안녕하세요".repeat(21));
    }

    private static CreateUserCommand buildCommand(String name, String phone, int age,
            Set<String> interests, String mbti,
            String intro) {
        return new CreateUserCommand(name, phone, age, interests, mbti, intro);
    }
}