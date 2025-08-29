package com.icebreaker.be.fixture;

import com.icebreaker.be.domain.interest.Interest;
import com.icebreaker.be.domain.interest.InterestType;
import com.icebreaker.be.domain.user.MbtiType;
import com.icebreaker.be.domain.user.User;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserFixture {

    public static final String DEFAULT_NAME = "홍길동";
    public static final String DEFAULT_PHONE = "01012345678";
    public static final MbtiType DEFAULT_MBTI = MbtiType.INTJ;

    public static User defaultUser() {
        return buildUser(DEFAULT_NAME, DEFAULT_PHONE, 25, List.of(InterestType.BOOKS, InterestType.MOVIES), MbtiType.INTJ, "안녕하세요");
    }

    public static User userWithId(Long id, String name, String phone) {
        User user = buildUser(name, phone, 25, List.of(InterestType.BOOKS, InterestType.MOVIES), MbtiType.INTJ, "안녕하세요");
        setId(user, id);
        return user;
    }

    private static User buildUser(String name, String phone, int age, List<InterestType> interests, MbtiType mbti, String intro) {
        return User.builder()
                .name(name)
                .phone(phone)
                .age(age)
                .interestTypes(interests)
                .mbti(mbti)
                .introduction(intro)
                .build();
    }

    private static void setId(User user, Long id) {
        try {
            Field field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
